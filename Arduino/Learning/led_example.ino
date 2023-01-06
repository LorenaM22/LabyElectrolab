const int LEDPIN3 = 4;
const int LEDPIN4 = 5;

void setup() {
  // put your setup code here, to run once:
  pinMode(LEDPIN3 , OUTPUT);
  pinMode(LEDPIN4 , OUTPUT);
}

void loop() {
  digitalWrite(LEDPIN3, HIGH);   // turn the LED on (HIGH is the voltage level)
  digitalWrite(LEDPIN4, HIGH);   // turn the LED on (HIGH is the voltage level)
  delay(250);                   // wait for a second
  digitalWrite(LEDPIN3, LOW);    // turn the LED off by making the voltage LOW
  digitalWrite(LEDPIN4, LOW);    // turn the LED off by making the voltage LOW
  delay(250);
  digitalWrite(LEDPIN3, HIGH);    // turn the LED on by making the voltage HIGH
  digitalWrite(LEDPIN4, LOW);    // turn the LED off by making the voltage LOW
  delay(250);
  digitalWrite(LEDPIN3, LOW);    // turn the LED off by making the voltage LOW
  digitalWrite(LEDPIN4, HIGH);    // turn the LED on by making the voltage HIGH
  delay(250);
  digitalWrite(LEDPIN3, LOW);    // turn the LED off by making the voltage LOW
  digitalWrite(LEDPIN4, LOW);    // turn the LED off by making the voltage LOW
  delay(250);

}
