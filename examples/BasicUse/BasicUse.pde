import dlp.interpolation.*;

int FRAME_RATE = 60;

Curve posCurve, satCurve, bnessCurve;
float startX, stopX, startS, stopS, startB, stopB;

void setup() {
  size(800, 600);
  frameRate(FRAME_RATE);
  colorMode(HSB);
  smooth();
  
  startX = 100.0;
  stopX = width - startX;
  posCurve = new Curve(this, startX, stopX, 1 * FRAME_RATE, Curve.EASE_OUT);
  posCurve.start();
  
  startS = 0.0;
  stopS = 192.0;
  satCurve = new Curve(this, startS, stopS, 1 * FRAME_RATE, Curve.EASE_OUT);
  satCurve.start();
  
  startB = 32.0;
  stopB = 208.0;
  bnessCurve = new Curve(this, startB, stopB, 1 * FRAME_RATE, Curve.EASE_OUT);
  bnessCurve.start();
}

void draw() {
  background(16);
  
  stroke(255);
  strokeWeight(3);
  
  fill(137, satCurve.value(), bnessCurve.value());
  
  ellipse(posCurve.value(), height/2, 80, 80);
}

void mousePressed() {
  posCurve.reverse();
  satCurve.reverse();
  bnessCurve.reverse();
}
