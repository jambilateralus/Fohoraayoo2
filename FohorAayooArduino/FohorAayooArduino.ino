#include "SIM900.h"
#include <SoftwareSerial.h>
#include "inetGSM.h"
#include <TinyGPS.h>

/*
 * Fohoraayoo
 * Date: Nov 10,2016
 * 
 * Wiring:
 * -------------------------
 * GPS RX = pin number 5
 * GPS TX = pin number 4
 * SIM900 RX = pin number 3
 * SIM900 TX =  pin number 2
 * LED = pin number 13
 * -------------------------
 * 
 * 
 * Libraries used:
 * ------------------------------------------------------------------------------
 * TinyGPS. src: https://github.com/mikalhart/TinyGPS
 * ITEADLIB_Arduino_SIMCom. src: https://github.com/itead/ITEADLIB_Arduino_SIMCom
 * -------------------------------------------------------------------------------
 * 
 * Note:
 * Change pinNumbers in GSM.cpp.
 * Tx: 2
 * Rx: 3
 */

TinyGPS gps;
SoftwareSerial gpsSerial(4, 5);

char msg[50];
int numdata;
boolean started=false;
InetGSM inet;



void setup()
{
     // Serial connection.
     Serial.begin(9600);
     // Initialize GPRS
     initGprs();
     // Initialize pin for led. 
     pinMode(LED_BUILTIN, OUTPUT);
};


void loop()
{
  sendGpsLocation();  
};


/*------------------------------------------------------
 * Reads GPS data from gps module and sends it to server
 * -----------------------------------------------------*/
void sendGpsLocation(){
  // Start serial communication with GPS module
  gpsSerial.begin(9600);

  // Some local variables.
  bool newData = false;
  unsigned long chars;
  unsigned short sentences, failed;

  // For one second we parse GPS data and report some key values
  for (unsigned long start = millis(); millis() - start < 1000;)
  {
    while (gpsSerial.available())
    {
      char c = gpsSerial.read();
      if (gps.encode(c)) // Did a new valid sentence come in?
        newData = true;
    }
  }

  // If new GPS data is received..
  if (newData)
  {
    Serial.println("** New GPS data received **");
    
    // Store latitude and longitude from gps to string variables.
    float flat, flon;
    String gpsLat, gpsLon;
    unsigned long age;
    gps.f_get_position(&flat, &flon);
    gpsLat = String(flat == TinyGPS::GPS_INVALID_F_ANGLE ? 0.0 : flat, 6);
    gpsLon = String(flon == TinyGPS::GPS_INVALID_F_ANGLE ? 0.0 : flon, 6);

    // Print GPS location
    Serial.println ("Latitude: " +gpsLat+ "  Longitude: " +gpsLon);
    
    Serial.println("** Sending GPS data to server **");

    // End serial communication with GPS module.
    gpsSerial.end();

    // Send GPS data to server.
    sendDataToServer(gpsLat,gpsLon);
  }

  // Else no new data found.
  else{
  gps.stats(&chars, &sentences, &failed);
  if (chars == 0)
    Serial.println("** No characters received from GPS: check wiring **");

  // End serial communication with GPS module.
  gpsSerial.end();    
  }
}


/*--------------------------
 * Initialize GPRS service
 * -------------------------*/
void initGprs(){
  Serial.println("** Initialing GPRS service **");
    // Start serial communication with GSM module
     if (gsm.begin(9600)) {
          Serial.println("\nStatus=READY");
          started=true;
     } else Serial.println("\nStatus=IDLE");
 
     // If sucessfully started... 
     if(started) {
          // Attach, put in order APN, username and password.
          if (inet.attachGPRS("web", "", ""))
               Serial.println("status=ATTACHED");
          else 
               Serial.println("status=ERROR");
          
          delay(1000);

          // Read IP address.
          gsm.SimpleWriteln("AT+CIFSR");
          delay(5000);
          
          // Read until serial buffer is empty.
          gsm.WhileSimpleRead();

          // End serial communication with GSM module
          SoftwareSerial(9,10).end();  
  }
}


/*---------------------------------
 * Send data to server
 * --------------------------------*/
void sendDataToServer(String latitude, String longitude){
     // Start serial communication with gsm module.
     gsm.begin(9600);

     // Convert string to char array
     String req = "/?lat="+latitude+"&lon="+longitude;
     char request[50];
     req.toCharArray(request, 50);

     // Send request to server
     numdata=inet.httpGET("fohoraayo.000webhostapp.com", 80, request, msg, 50);
    

     // If request is successfully sent, turn on led for 1 sec.
     if(numdata!=0){
      turnOnLed(1000);
      }

     // Print the results.
     Serial.println("\nData received:");
     Serial.println(msg);
     
     // End serial communication with gsm module
     SoftwareSerial(9,10).end();  
}


/*--------------------------------
 * Turn on led for 'duration' ms.
 * -------------------------------*/
void turnOnLed(int duration){
      digitalWrite(LED_BUILTIN, HIGH);  
      delay(1000);
      digitalWrite(LED_BUILTIN, LOW);
 }

