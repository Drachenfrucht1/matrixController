#include <MemoryFree.h>
#include <pgmStrToRAM.h>
#include <Adafruit_NeoPixel.h>
#include <Adafruit_NeoMatrix.h>
#include <Adafruit_GFX.h>

#define DIN = 6;
//const int width = 13;
//const int height = 10;

#define ledsCount 9;

//String color, r, g, b;

const int byteLength = width * height * 3 + 1;

/*Adafruit_NeoMatrix matrix = Adafruit_NeoMatrix(width, height, DIN,
  NEO_MATRIX_TOP     + NEO_MATRIX_RIGHT +
  NEO_MATRIX_ROWS + NEO_MATRIX_PROGRESSIVE,
  NEO_RGB            + NEO_KHZ800);*/

void setup() {
  /*matrix.begin();
  matrix.show();*/
  Serial.begin(250000);
}

void loop() {
  if(Serial.available() > 0) {
    int time1 = millis();
    char data[byteLength];
    Serial.readBytesUntil('!', data, byteLength);
    Serial.flush();
    
    int i = 0;
    for(int x = 0; x < width; x++) {
      for(int y = 0; y < height; y++) {
        int time2 = millis();
        color = split(data, ':', i);
        r = split(color, '.', 0);
        g = split(color, '.', 1);
        b = split(color, '.', 2);
        matrix.drawPixel(x, y, matrix.Color(r.toInt(), g.toInt(), b.toInt()));
        i++;
      }
    }
    matrix.show();
    int t = millis() -time1;
    Serial.println(freeMemory(), DEC);
  }
}

String split(String input, char separator, int index) {
  int found = 0;
  int strIndex[] = {0, -1};
  int maxIndex = input.length()-1;

  for(int i=0; i<=maxIndex && found<=index; i++){
    if(input.charAt(i)==separator || i==maxIndex){
        found++;
        strIndex[0] = strIndex[1]+1;
        strIndex[1] = (i == maxIndex) ? i+1 : i;
    }
  }
  return found>index ? input.substring(strIndex[0], strIndex[1]) : "";
}
