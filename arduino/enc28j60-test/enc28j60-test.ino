// Demo using DHCP and DNS to perform a web client request.
// 2011-06-08 <jc@wippler.nl> http://opensource.org/licenses/mit-license.php

#include <SPI.h>
#include <EtherCard.h>

#define BUFFER_SIZE 700

// ethernet interface mac address, must be unique on the LAN
static byte mymac[] = { 0x74,0x69,0x69,0x2D,0x30,0x31 };

byte Ethernet::buffer[BUFFER_SIZE];

const char website[] PROGMEM = "fbcdn-sphotos-c-a.akamaihd.net";

unsigned long startTime = 0L;
unsigned long downloadSize = 0L;

// called when the client request is complete
static void my_callback (byte status, word off, word len) {
  Serial.print("Status: ");
  Serial.println(status, DEC);
  if (status > 1) {
    downloadSize += len;
    unsigned long duration = millis() - startTime;
    Serial.print(">>> ");
    Serial.print(duration, DEC);
    Serial.print(" ");
    Serial.print(status, DEC);
    Serial.print(" ");
    Serial.print(downloadSize, DEC);
    Serial.print(" ");
    Serial.print(off, DEC);
    Serial.print(" ");
    Serial.print(len, DEC);
    Serial.print(" ");
    Serial.print(Ethernet::buffer[off+len-1], DEC);
    if (!Ethernet::buffer[off+len-1]) {
      for (int i=5;i>0;i--) {
        Serial.println(i);
        delay(1000);
      }
      request();
    }
  } else {
    Serial.println("");
    for (int i=5;i>0;i--) {
      Serial.println(i);
      delay(1000);
    }
    request();
  }
}

void setup () {
  Serial.begin(57600);
  Serial.println(F("\n[webClient]"));

  if (ether.begin(sizeof Ethernet::buffer, mymac, 53) == 0)
    Serial.println(F("Failed to access Ethernet controller"));
  if (!ether.dhcpSetup())
    Serial.println(F("DHCP failed"));

  ether.printIp("IP:  ", ether.myip);
  ether.printIp("GW:  ", ether.gwip); 
  ether.printIp("DNS: ", ether.dnsip);  

  if (!ether.dnsLookup(website))
    Serial.println("DNS failed");
    
  ether.printIp("SRV: ", ether.hisip);
  ether.persistTcpConnection(true);
  
  request();
}

void loop () {
  int plen = ether.packetReceive();
  //Serial.println(plen, DEC);
  ether.packetLoop(plen);
}

void request() {
  Serial.println();
  Serial.println("<<< REQ ");
  startTime = millis();
  downloadSize = 0;
  ether.browseUrl(PSTR("/hphotos-ak-xfa1/t31.0-8/s960x960/10714533_10152508983771752_8692041707642229992_o.jpg"), "", website, my_callback);
}
