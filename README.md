RaspberryPi installation:
install mosquitto
install maven
raspi-config -> enable ssh, expand sd card
router -> assign raspberry PI static IP
sudo vi /etc/systemd/system/SmartHome.service -- create service

  [Unit]
  Description=SmartHome application
  After=syslog.target

  [Service]
  User=pi
  ExecStart=/etc/SmartHome/SmartHome.war
  SuccessExitStatus=143

  [Install]
  WantedBy=multi-user.target
        
sudo systemctl enable SmartHome.service -- autostart service
git clone https://github.com/Kesop1/SmartHome-2.0.git
sudo chmod +x /home/pi/SmartHome/SmartHome-2.0/misc/deploy_SmartHome***.sh
run /home/pi/SmartHome/SmartHome-2.0/misc/deploy_SmartHome***.sh
