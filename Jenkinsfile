pipeline {
    agent any

    tools {
        gradle "gradle 6.4.1"
    }

    stages {
        stage("Build") {
            steps {
				echo "export ANDROID_HOME=/home/levan/Android/Sdk"
				echo "export PATH=$PATH:$ANDROID_HOME/tools"
				sh "gradle -v"
				sh "./gradlew clean"
                sh "./gradlew build"
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
