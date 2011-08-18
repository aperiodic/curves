/**
 * An example illustrating the difference between reverse() and swapEndpoints()
 * for curves that are not symmetric, such as EASE_OUT curves.
 * Click to reverse/swap.
 * The red ball on top uses reverse(), while the blue one on bottom uses
 * swapEndpoints().
 * Note that while the red ball follows an EASE_OUT curve while travelling from
 * left to right, it effectively follows an EASE_IN curve on the way back, after
 * it has been reversed. 
 * The blue ball, however, follows an EASE_OUT curve both ways.
 * See the documentation for reverse for a more thorough explanation of this 
 * phenomenon.
 */


import dlp.interpolation.*;

int FRAME_RATE = 60;

Bubble reverseBubble, swapBubble;


void setup() {
  size(800, 600);
  frameRate(FRAME_RATE);
  colorMode(HSB);
  smooth();
  
  reverseBubble = new Bubble(this, 9, height/3);
  swapBubble = new Bubble(this, 137, 2 * height/3);
}


void draw() {
  background(16);
  
  swapBubble.draw();
  reverseBubble.draw();
}


void mousePressed() {
  if (reverseBubble.finished() && swapBubble.finished()) {
    reverseBubble.reverse();
    reverseBubble.start();
    
    swapBubble.swapEndpoints();
    swapBubble.start();
  }
}


class Bubble {
  Curve posCurve, satCurve, bnessCurve;
  float startX, stopX, startS, stopS, startB, stopB;
  int _hue, _y;
  
  Bubble (PApplet parent, int hue, int y) {
    _hue = hue;
    _y = y;
    
    startX = 100.0;
    stopX = width - startX;
    posCurve = new Curve(parent, startX, stopX, 1 * FRAME_RATE, Curve.EASE_OUT);
    posCurve.start();
    
    startS = 0.0;
    stopS = 192.0;
    satCurve = new Curve(parent, startS, stopS, 1 * FRAME_RATE, Curve.EASE_OUT);
    satCurve.start();
    
    startB = 32.0;
    stopB = 208.0;
    bnessCurve = new Curve(parent, startB, stopB, 1 * FRAME_RATE, Curve.EASE_OUT);
    bnessCurve.start();
  }
  
  boolean finished() {
    return posCurve.finished() && satCurve.finished() && bnessCurve.finished();
  }
  
  void swapEndpoints() {
    posCurve.swapEndpoints();
    satCurve.swapEndpoints();
    bnessCurve.swapEndpoints();
  }
  
  void reverse() {
    posCurve.reverse();
    satCurve.reverse();
    bnessCurve.reverse();
  }
  
  void start() {
    posCurve.start();
    satCurve.start();
    bnessCurve.start();
  }
  
  void draw() {
    stroke(255);
    strokeWeight(3);
    
    fill(_hue, satCurve.value(), bnessCurve.value());
    
    ellipse(posCurve.value(), _y, 80, 80);
  }
}
