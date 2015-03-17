/*
  Repeating Web client

 This sketch connects to a a web server and makes a request
 using a Wiznet Ethernet shield. You can use the Arduino Ethernet shield, or
 the Adafruit Ethernet shield, either one will work, as long as it's got
 a Wiznet Ethernet module on board.

 This example uses DNS, by assigning the Ethernet client with a MAC address,
 IP address, and DNS address.

 Circuit:
 * Ethernet shield attached to pins 10, 11, 12, 13

 created 19 Apr 2012
 by Tom Igoe
 modified 21 Jan 2014
 by Federico Vanzati

 http://arduino.cc/en/Tutorial/WebClientRepeating
 This code is in the public domain.

 */

#include <SPI.h>
#include <Ethernet.h>

// assign a MAC address for the ethernet controller.
// fill in your address here:
byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xEF
};
// fill in an available IP address on your network here,
// for manual configuration:
IPAddress ip;


// fill in your Domain Name Server address here:
IPAddress myDns(1, 1, 1, 1);

// initialize the library instance:
EthernetClient client;

char server[] = "i.imgur.com";
//IPAddress server(64,131,82,241);

unsigned long lastConnectionTime = 0;             // last time you connected to the server, in milliseconds
const unsigned long postingInterval = 60L * 1000L; // delay between updates, in milliseconds
// the "L" is needed to use long type numbers

void setup() {
  // start serial port:
  Serial.begin(57600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }

  // give the ethernet module time to boot up:
  delay(1000);
  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    // no point in carrying on, so do nothing forevermore:
    while (true);
  }

  // start the Ethernet connection using a fixed IP address and DNS server:
  // TODO: do I need this?
  //Ethernet.begin(mac, ip, myDns);
  
  
  // print the Ethernet board/shield's IP address:
  Serial.print("My IP address: ");
  Serial.println(Ethernet.localIP());
  
  SPI.setClockDivider(16);
}

void loop() {
  // if there's incoming data from the net connection.
  // send it out the serial port.  This is for debugging
  // purposes only:
  httpRequest();
  
  while(1);
}

// this method makes a HTTP connection to the server:
void httpRequest() {
  // close any connection before send a new request.
  // This will free the socket on the WiFi shield
  client.stop();

  // if there's a successful connection:
  if (client.connect(server, 80)) {
    Serial.println("connecting...");
    // send the HTTP PUT request:
    client.println("GET /o7Jh621.png HTTP/1.1");
    client.println("Host: i.imgur.com");
    client.println("User-Agent: arduino-ethernet");
    client.println("Connection: close");
    client.println();
    
    unsigned long startTime = millis();
    unsigned long fileSize = 0;
    while (!client.available());
    while (client.connected()) {
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
      fileSize += client.read() > 0;
    }
    unsigned long duration = millis() - startTime;
    double downloadSpeed = fileSize * 8.0 / 1048.576 / duration;
    Serial.print("Duration: ");
    Serial.println(duration, DEC);
    Serial.print("Done: ");
    Serial.println(client.connected() ? "false" : "true");
    Serial.print("File size: ");
    Serial.println(fileSize, DEC);
    Serial.print("Speed Mbps: ");
    printDouble(downloadSpeed, 100);
    Serial.print("Speed Kbps: ");
    printDouble(downloadSpeed * 1000, 100);
    Serial.println("");
  } else {
    // if you couldn't make a connection:
    Serial.println("connection failed");
  }
}

void printDouble( double val, unsigned int precision){
// prints val with number of decimal places determine by precision
// NOTE: precision is 1 followed by the number of zeros for the desired number of decimial places
// example: printDouble( 3.1415, 100); // prints 3.14 (two decimal places)

   Serial.print (int(val));  //prints the int part
   Serial.print("."); // print the decimal point
   unsigned int frac;
   if(val >= 0)
       frac = (val - int(val)) * precision;
   else
       frac = (int(val)- val ) * precision;
   Serial.println(frac,DEC);
} 


