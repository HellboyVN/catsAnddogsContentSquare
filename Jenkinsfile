pipeline {
    agent any

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
