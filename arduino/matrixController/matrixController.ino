#include <MemoryFree.h>
//#include <pgmStrToRAM.h>
#include <Adafruit_NeoPixel.h>
#include <Adafruit_NeoMatrix.h>
#include <Adafruit_GFX.h>

const int DIN = 6;
const int width = 13;
const int height = 2;

int r, g, b;

const int byteLength = width * height * 3 + 1;

Adafruit_NeoMatrix matrix = Adafruit_NeoMatrix(width, height, DIN,
  NEO_MATRIX_TOP     + NEO_MATRIX_RIGHT +
  NEO_MATRIX_ROWS + NEO_MATRIX_PROGRESSIVE,
  NEO_RGB            + NEO_KHZ800);

void setup() {
  matrix.begin();
  matrix.show();
  Serial.begin(250000);
}

void loop() {
  if(Serial.available() > 0) {
    int time1 = millis();
    byte data[byteLength];
    Serial.readBytesUntil(1, data, byteLength);
    Serial.flush();
    
    int i = 0;
    for(int x = 0; x < width; x++) {
      for(int y = 0; y < height; y++) {
        int time2 = millis();
        r = (int) data[i];
        i++;
        g = (int) data[i];
        i++;
        b = (int) data[i];
        i++;
        matrix.drawPixel(x, y, matrix.Color(r, g, b));
      }
    }
    matrix.show();
    int t = millis() -time1;
    Serial.println(freeMemory(), DEC);
  }
}
