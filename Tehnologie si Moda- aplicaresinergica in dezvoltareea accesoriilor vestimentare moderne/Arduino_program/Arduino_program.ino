#include <HX711.h>
#include <SoftwareSerial.h>
#include <Adafruit_NeoPixel.h>
#ifdef __AVR__
  #include <avr/power.h>
#endif
#define NUM_LEDS 120
#define PIN 6
#define DOUT 8
#define CLK 7
HX711 scale;
float calibration_factor = -18000;
String command;

SoftwareSerial RFID(2,3); rx
int data1 = 0;
int ok = -1;
int yes = 13;
int no = 12;
 
int tag1[14] = {2,50,52,48,48,55,55,51,68,57,52,70,65,3};
int tag2[14] = {2,48,56,48,48,69,68,56,65,54,53,48,65,3};
int newtag[14] = { 0,0,0,0,0,0,0,0,0,0,0,0,0,0}; // used for read comparisons

Adafruit_NeoPixel strip (NUM_LEDS, PIN, NEO_RGB + NEO_KHZ400);

void setup() {
  RFID.begin(9600);//initialisation of RFID
  Serial.begin(9600);
  strip.begin();
  strip.show(); //turn off all pixels

  //for weight
  scale.begin(8,7);
  scale.set_scale(-40000);
  scale.tare();
 
}

void loop() {

  if (Serial.available()) {
    readTags();
    Serial.println("W"+String(scale.get_units(),3));
    char tmp = Serial.read();
    if (tmp == '#') {
      Serial.println(command);
      runCommand(command);
      command = "";
    } else {
      command += tmp;
    }
  }
}

void runCommand(String command) {
  Serial.println(command);

if (command.startsWith("color_led:")) {
    command = command.substring(command.indexOf(':') + 1);
       setColor(command);
  }
}
String getValue(String data, char separator, int index) {
  int found = 0;
  int strIndex[] = { 0, -1 };
  int maxIndex = data.length() - 1;
 
  for (int i = 0; i <= maxIndex && found <= index; i++) {
    if (data.charAt(i) == separator || i == maxIndex) {
      found++;
      strIndex[0] = strIndex[1] + 1;
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }
  return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}

// color_led:255;54;0
void setColor(String colorStr){
  int red = getValue(colorStr, ';', 0).toInt();
  int green = getValue(colorStr, ';', 1).toInt();
  int blue = getValue(colorStr, ';', 2).toInt();
  colorWipe(strip.Color(red, green, blue), 50);
  //theaterChase(strip.Color(red, green, blue), 10);
}
void colorWipe(uint32_t color, int wait) {
  for(int i=0; i<120; i++) { // For each pixel in strip...
    strip.setPixelColor(i, color);         //  Set pixel's color (in RAM)
    strip.show();                          //  Update strip to match
    delay(wait);                           //  Pause for a moment
  }
}

boolean comparetag(int aa[14], int bb[14])
{
  boolean ff = false;
  int fg = 0;
  for (int cc = 0 ; cc < 14 ; cc++)
  {
    if (aa[cc] == bb[cc])
    {
      fg++;
    }
  }
  if (fg == 14)
  {
    ff = true;
  }
  return ff;
}
void checkmytags() // compares each tag against the tag just read
{
  ok = 0; // this variable helps decision-making,
  // if it is 1 we have a match, zero is a read but no match,
  // -1 is no read attempt made
  if (comparetag(newtag, tag1) == true)
  {
    ok=1;
  }
  if (comparetag(newtag, tag2) == true)
  {
    ok=2;
  }
}
void readTags()
{
  ok = -1;
 
  if (RFID.available() > 0) 
  {
    // read tag numbers
    delay(100); // needed to allow time for the data to come in from the serial buffer.
 
    for (int z = 0 ; z < 14 ; z++) // read the rest of the tag
    {
      data1 = RFID.read();
      newtag[z] = data1;
    }
    RFID.flush(); // stops multiple reads
 
    // do the tags match up?
    checkmytags();
  }
 
  // now do something based on tag type
  if (ok == 1) // if we had a match
  {
    Serial.println("Chei");
    ok = -1;
  }
  else if (ok == 2) // if we didn't have a match
  {
    Serial.println("Portmoneu"); 
    ok = -1;
  }
}
