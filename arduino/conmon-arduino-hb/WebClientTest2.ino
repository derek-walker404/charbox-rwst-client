/*
  Web client

 This sketch connects to a website (http://www.google.com)
 using an Arduino Wiznet Ethernet shield.

 Circuit:
 * Ethernet shield attached to pins 10, 11, 12, 13

 created 18 Dec 2009
 by David A. Mellis
 modified 9 Apr 2012
 by Tom Igoe, based on work by Adrian McEwen

 */

#include <SPI.h>
#include <Ethernet.h>

#define BUFFER_LENGTH 1024

// Enter a MAC address for your controller below.
// Newer Ethernet shields have a MAC address printed on a sticker on the shield
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
// if you don't want to use DNS (and reduce your sketch size)
// use the numeric IP instead of the name for the server:
//IPAddress server(74,125,232,128);  // numeric IP for Google (no DNS)
char server[] = "i.imgur.com";    // name address for Google (using DNS)

// Set the static IP address to use if the DHCP fails to assign
IPAddress ip(192, 168, 0, 177);

// Initialize the Ethernet client library
// with the IP address and port of the server
// that you want to connect to (port 80 is default for HTTP):
EthernetClient client;

unsigned char buf[BUFFER_LENGTH];

unsigned long startTime;

void setup() {
  // Open serial communications and wait for port to open:
  Serial.begin(57600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }

  // start the Ethernet connection:
  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    // no point in carrying on, so do nothing forevermore:
    // try to congifure using IP address instead of DHCP:
    Ethernet.begin(mac, ip);
  }
  // give the Ethernet shield a second to initialize:
  delay(1000);
  time();
}


unsigned long downloadSize = 0;

void loop()
{
  downloadSize += client.read(buf, BUFFER_LENGTH);
  
  // if the server's disconnected, stop the client:
  if (!client.connected()) {
    unsigned long duration = millis() - startTime;
    report(downloadSize, duration);
    client.stop();
    // do nothing forevermore:
    while (true);
  }
}

void time() {
  Serial.println("connecting...");

  // if you get a connection, report back via serial:
  if (client.connect(server, 80)) {
    Serial.println("connected");
    // Make a HTTP request:
    client.println("GET /o7Jh621.png HTTP/1.1");
    client.println("Host: i.imgur.com");
    client.println("Connection: close");
    client.println();
    startTime = millis();
  }
  else {
    // kf you didn't get a connection to the server:
    Serial.println("connection failed");
  }
}

void report(unsigned long downloadSize, unsigned long duration) {
  Serial.println();
  Serial.println("disconnecting.");
  Serial.print("Size: ");
  Serial.println(downloadSize, DEC);
  Serial.print("Duration: ");
  Serial.println(duration, DEC);
  double downloadSpeed = downloadSize * 8000.0 / 1048.576 / duration;
  Serial.print("Speed: ");
  Serial.println((int)downloadSpeed, DEC);
}

