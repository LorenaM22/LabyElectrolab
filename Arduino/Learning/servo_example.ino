#include <Servo.h>

Servo myservo;

const int PINSERVO = 8;

void setup() {
  // put your setup code here, to run once:
  myservo.attach(PINSERVO); 
}

void loop() {
  // put your main code here, to run repeatedly:
  myservo.write(20);      
  delay(1000);
  myservo.write(40);      
  delay(1000);
  myservo.write(60);      
  delay(1000);
  myservo.write(80);      
  delay(1000);
  myservo.write(100);      
  delay(1000);
  myservo.write(120);      
  delay(1000);
  myservo.write(140);      
  delay(1000);
  myservo.write(160);      
  delay(1000);
  myservo.write(180);      
  delay(1000);
  myservo.write(0);
}
