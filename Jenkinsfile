pipeline {
    agent any

    tools {
        gradle "6.4.1"
    }

    stages {
        stage("Build") {
            steps {
                sh "gralde -v"
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