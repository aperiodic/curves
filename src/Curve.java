package dlp.interpolation;
import processing.core.*;

/**
 * Easy-to-use interpolation curves, in four varieties. 
 * Once a curve is created, call the {@link #start} method to begin the
 * interpolation.
 * While running, the interpolation can be {@link #reverse}'d.
 * Once finished, the curve can be turned around with {@link #swapEndpoints}, or
 * new endpoints can be set with {@link #setStartValue} and 
 * {@link #setTargetValue}.
 * Start the animation again with {@link #start}.
 * If you just want to re-live old times, play the whole thing back with 
 * {@link #restart}.
 * If you like to live dangerously, you can even change the endpoints while the 
 * curve is interpolating (note: not actually dangerous).
 *
 * @author Dan Lidral-Porter
 * @version 0.1
 */
public class Curve {
	
  /**
   * A curve of constant slope (a straight line).
   */
  public static final int LINEAR = 0;
  
  /**
   * A curve that starts out slow, but ends up fast.
   */
  public static final int EASE_IN = 1;
  
  /**
   * A curve that starts out fast, but ends up slow.
   */
  public static final int EASE_OUT = 2;
  
  /**
   * A curve that starts out slow, zips through the middle, and then slows down
   * for a nice, easy finish.
   */
  public static final int EASE_IN_OUT = 3;
	
  
	private float _t, _start, _target, _value, _time, _frames;
	private int _curve;
	private boolean animating, finished, reversed;
	private PApplet _parent;
  
  
  /**
   * Create a curve from <code>0</code> to <code>1</code>, lasting for 
   * <code>frame</code> frames, with the indicated <code>curve</code> type.
   * 
   * @see #Curve(PApplet, float, float, int, int)
   * @see #LINEAR
   * @see #EASE_IN
   * @see #EASE_OUT
   * @see #EASE_IN_OUT
   */
	public Curve (PApplet parent, int frames, int curve) {
    this(parent, 0.0f, 1.0f, frames, curve); 
  }
  
  /**
   * Create a curve from <code>0</code> to <code>target</code>, lasting for 
   * <code>frame</code> frames, with the indicated <code>curve</code> type. 
   * 
   * @see #Curve(PApplet, float, float, int, int)
   * @see #LINEAR
   * @see #EASE_IN
   * @see #EASE_OUT
   * @see #EASE_IN_OUT
   */
	public Curve (PApplet parent, float target, int frames, int curve) {
    this(parent, 0.0f, target, frames, curve);
  }
	
  /**
   * Create a curve from <code>start</code> to <code>target</code>, lasting for
   * <code>frame</code> frames, with the indicated <code>curve</code> type.
   * 
   * @param parent The host sketch object, typically "this" unless you're doing
   *               something crazy.
   * @param start The initial value of the interpolation.
   * @param target The final value of the interpolation.
   * @param frames The number of frames that the interpolation will last.
   * @param curve The type of interpolation curve (e.g. Curve.EASE_IN).
   * 
   * @see #LINEAR
   * @see #EASE_IN
   * @see #EASE_OUT
   * @see #EASE_IN_OUT
   */
	public Curve (PApplet parent, float start, float target, int frames, int curve) {
		_parent = parent;
		_t = 0.0f;
		_start = start;
		_value = start;
		_target = target;
		_frames = frames;
		_curve = curve;
		
		animating = false;
		finished = false;
		reversed = false;
		
		_parent.registerPre(this);
	}
	
	public void pre() {
		if (!animating || finished) {
			return;
		}
		
    if (!reversed) {
      _t += 1.0f / _frames;
    } else {
      _t -= 1.0f / _frames;
    }
		
		if (_t >= 1.0f) {
      _t = 1.0f;
			_value = _target;
			finished = true;
      animating = false;
			return;
		} else if (_t <= 0.0f) {
      _t = 0.0f;
			_value = _start;
      finished = true;
      animating = false;
			return;
		}
		
		if (_curve == LINEAR) {
			_value = (1.0f - _t)*_start + _t*_target;
		} else if (_curve == EASE_IN) {
			_value = (1.0f - _t)*(1.0f - _t)*_start + 2*_t*(1.0f - _t)*_start +
               _t*_t*_target;
		} else if (_curve == EASE_OUT) {
			_value = (1.0f - _t)*(1.0f - _t)*_start + 2*_t*(1.0f - _t)*_target + 
               _t*_t*_target;
		} else if (_curve == EASE_IN_OUT) {
			_value = (1.0f - _t)*(1.0f - _t)*(1.0f - _t)*_start + 
               (1.0f - _t)*(1.0f - _t)*3*_t*_start + 
               3*_t*_t*(1.0f - _t)*_target + _t*_t*_t*_target;
		}
	}

  /**
   * Set the initial value of the curve (i.e., the value when 
   * <code>t = 0</code>).
   * 
   * If the curve is not currently interpolating, also {@link #reset} the 
   * interpolation.
   * 
   * @param value The new initial value. 
   */
	public void setStartValue(float value) {
		_start = value;
    if (!animating) {
      reset();
    }
	}
	
  /**
   * Set the target value of the curve (i.e., the value when
   * <code>t = 1</code>).
   * 
   * If the curve is not currently interpolating, also {@link #reset} the 
   * interpolation.
   * 
   * @param value The new target value.
   */
	public void setTargetValue(float value) {
		_target = value;
    if (!animating) {
      reset();
    }
	}
  
  /**
   * Set the duration of the curve's interpolation, in frames.
   * 
   * If the curve is not currently interpolating, also {@link #reset} the
   * interpolation.
   * 
   * @param frames The number of frames that the interpolation should last for.
   */
  public void setDuration(int frames) {
    _frames = frames;
  }
	
  /**
   * Start the curve's interpolation. 
   * 
   * Note that if the curve has finished, but its initial or target values 
   * have not been changed and it has not reversed, then this will do nothing.
   * If you want to just rewind the curve and play it back from the beginning, 
   * use {@link #restart}.
   */
	public void start() {
		animating = true;
		finished = false;
	}
	
  /**
   * Toggle the curve's interpolation. 
   * 
   * If the curve is not interpolating, it will start, and vice-versa. Does not
   * reset or restart the interpolation. Think of this as a play/pause button.
   */
	public void toggle() {
		animating = !animating;
	}
  
  /**
   * Reverse the direction of interpolation (whether <code>t</code> is 
   * increasing or decreasing).
   * 
   * If the curve is currently interpolating, this will take effect immediately.
   * If the curve has finished interpolating, you will need to {@link #start} it
   * again.
   * 
   * <p>
   * Note that reversing a curve is not quite the same as swapping the 
   * endpoints, if the curve is not symmetric about the midpoint.
   * For example, an ease-in curve starts out slowly and gets faster as time 
   * goes on; it is not symmetric about the midpoint, since it's going faster
   * at <code>t = 0.9</code> than at <code>t = 0.1</code>, even though both
   * times are the same distance from <code>t = 0.5</code>.
   * If you reverse this curve, then <code>t = 0.9</code> near the beginning,
   * rather than the end, of the interpolation.
   * Since this is an ease-in curve, it will be going quickly at this point, but
   * when <code>t = 0.1</code> (near the end), it will be going slowly. 
   * Long story short, this reversed ease-in curve behaves as if it were an 
   * un-reversed ease-out curve with the endpoints swapped!
   * </p>
   * 
   * <p>
   * For an example which might make the above explanation clear, see the 
   * ReverseVsSwap.pde file in the examples directory.
   * </p>
   * 
   * @see #swapEndpoints
   */
  public void reverse() {
    reversed = !reversed;
  }
  
	
  /**
   * If the interpolation has finished, swap the start and target values, and
   * reset the curve.
   * 
   * <p>
   * Note that, for some curves, this is not quite the same as reversing.
   * See {@link #reverse} for a more thorough discussion.
   * </p>
   */
	public void swapEndpoints() {
    if (finished) {
      float temp = _start;
      _start = _target;
      _target = temp;
      reset();
    }
	}
  
  /**
   * Stop the interpolation and reset it back to the beginning, but don't 
   * start it again.
   **/
  public void reset() {
    _t = 0.0f;
    animating = false;
    finished = false;
    _value = _start;
  }
	
  /**
   * Restart the interpolation from the beginning.
   * 
   * No matter the current state, the curve will be interpolating after calling
   * this method.
   */
	public void restart() {
		_t = 0.0f;
		animating = true;
    finished = false;
    _value = _start;
	}
	
  /**
   * Get the current interpolation value of the curve.
   * 
   * @return A float in the range <code>[startValue, targetValue]</code>.
   */
	public float value() {
		return _value;
	}
	
  /**
   * Get the current time of the curve's interpolation.
   * 
   * @return A float in the range <code>[0, 1]</code>.
   */
	public float t() {
		return _t;
	}
  
  /**
   * Get whether or not the curve is currently interpolating.
   * 
   * @return A boolean indicating if the curve is interpolating.
   */
  public boolean animating() {
    return animating;
  }
  
  /**
   * Get whether or not the curve's interpolation has finished. 
   * 
   * Note that this is not quite the same as the negation of the return value of
   * {@link #animating()}, as this is true if and only if the interpolation has
   * been started, allowed to run its course, and is currently stopped (i.e., 
   * has not had {@link #reset}, {@link #restart}, or {@link #reverse} called upon
   * it since stopping). 
   * 
   * The upshot of this is you can create a curve without starting interpolation
   * until some point down the road, and anything that's waiting for the curve
   * to start and then finish interpolating can check for that condition with 
   * only this method.
   * 
   * @return A boolean indicating if the curve's interpolation is finished.
   */
	public boolean finished() {
		return finished;
	}
}
