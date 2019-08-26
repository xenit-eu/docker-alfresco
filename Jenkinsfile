pipeline {
    agent any
    stages {
        stage("Build Docker Image") {
            steps {
                sh "./gradlew -Penterprise -Plegacy buildDockerImage"
            }
        }

        stage("Unit Tests") {
            steps {
                sh "./gradlew -Penterprise test"
            }
        }

        stage("Integration Tests") {
            steps {
                sh "./gradlew -Penterprise integrationTests --info"
            }
            post {
                always {
                    sh "./gradlew -Penterprise composeDown"
                }
            }
        }

        stage("Publish Docker Image") {
            when {
                anyOf {
                    branch 'master'
                    branch 'release*'
                }
            }
            steps {
                sh "./gradlew -Penterprise -Plegacy pushDockerImage"
            }
        }
    }
}
