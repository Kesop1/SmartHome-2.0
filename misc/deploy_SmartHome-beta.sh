REPO_HOME=$HOME
PROJECT_NAME=SmartHome-2.0
PROJECT_VERSION=1.0-SNAPSHOT
SERVICE_NAME=SmartHome-beta
BRANCH_NAME=faza3
echo "REPO_HOME=$REPO_HOME"
echo "PROJECT_NAME=$PROJECT_NAME"
echo "---- Folders clean-up ----"
#sudo chown -R pi $REPO_HOME
rm -rf $REPO_HOME/$PROJECT_NAME
echo "---- Cloning the repoository ----"
git clone https://github.com/Kesop1/SmartHome-2.0.git $REPO_HOME/$PROJECT_NAME
cd $REPO_HOME/$PROJECT_NAME
git checkout $BRANCH_NAME
echo "---- Building the application ----"
mvn -f $REPO_HOME/$PROJECT_NAME clean install -DskipTests=true
echo "---- Deploying the application  ----"
service $SERVICE_NAME stop
rm /etc/$SERVICE_NAME/$PROJECT_NAME.war
rm /etc/$SERVICE_NAME/application.properties
mv $REPO_HOME/$PROJECT_NAME/target/$PROJECT_NAME-$PROJECT_VERSION.war /etc/$SERVICE_NAME/$PROJECT_NAME.war
mv $REPO_HOME/$PROJECT_NAME/misc/application.properties /etc/$SERVICE_NAME
chmod +x /etc/$SERVICE_NAME/$PROJECT_NAME.war
service $SERVICE_NAME start
rm -rf $REPO_HOME/$PROJECT_NAME
