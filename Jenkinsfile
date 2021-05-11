pipeline {
    agent any

    tools {
        gradle "gradle 6.4.1"
    }

    stages {
        stage("Build") {
            steps {
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
