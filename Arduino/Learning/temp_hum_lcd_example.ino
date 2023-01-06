#include <LiquidCrystal_I2C.h>
#include "DHT.h"

#define DHTTYPE DHT22 

const int DHTPin = 7;   

LiquidCrystal_I2C lcd(0x27,16,2);
DHT dht(DHTPin, DHTTYPE);

void setup() {

  lcd.begin();
  lcd.backlight();
  lcd.setCursor(0,0);
  dht.begin();

}

void loop() {
 // Reading temperature or humidity takes about 250 milliseconds!
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  
  if (isnan(h) || isnan(t)) {
      Serial.println("Failed to read from DHT sensor!");
      return;
  }
  lcd.setCursor(2, 0);
  lcd.print("Bienvenido a");
  lcd.setCursor(3, 1);
  lcd.print("Electrolab");
  delay(3000);
  lcd.clear();
  lcd.setCursor(2, 0);
  lcd.print("El sensor de");
  lcd.setCursor(0, 1);
  lcd.print("T(*C) y Hum (%)");
  delay(3000);
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("Esta abajo a la");
  lcd.setCursor(5, 1);
  lcd.print("derecha");
  delay(3000);
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("La sala esta a:");
  delay(3000);
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("T: "+ String(t)+" C");
  lcd.setCursor(0, 1);
  lcd.print("Hum: "+ String(h)+" %");
  delay(3000);
  lcd.clear();

}
