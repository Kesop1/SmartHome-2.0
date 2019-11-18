REPO_HOME=$HOME
PROJECT_NAME=SmartHome-2.0
PROJECT_VERSION=2.0
echo "REPO_HOME=$REPO_HOME"
echo "PROJECT_NAME=$PROJECT_NAME"
echo "---- Folders clean-up ----"
#sudo chown -R pi $REPO_HOME
rm -rf $REPO_HOME/$PROJECT_NAME
echo "---- Cloning the repoository ----"
sudo git clone https://github.com/Kesop1/$PROJECT_NAME.git $REPO_HOME/$PROJECT_NAME
echo "---- Building the application ----"
mvn -f $REPO_HOME/$PROJECT_NAME clean install -DskipTests=true
echo "---- Deploying the application  ----"
service SmartHome stop
rm /etc/SmartHome/$PROJECT_NAME.war
rm /etc/SmartHome/application.properties
mv $REPO_HOME/$PROJECT_NAME/target/$PROJECT_NAME-$PROJECT_VERSION.war /etc/SmartHome/$PROJECT_NAME.war
mv $REPO_HOME/$PROJECT_NAME/misc/application.properties /etc/SmartHome
chmod +x /etc/SmartHome/$PROJECT_NAME.war
service SmartHome start
rm -rf $REPO_HOME/$PROJECT_NAME
