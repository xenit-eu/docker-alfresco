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
                    sh "./gradlew -Penterprise composeDownAll"
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
    
    post {
        failure {
	    emailext body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\n More info at: ${env.BUILD_URL}",
                recipientProviders: [[$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']],
                subject: "Jenkins Build ${currentBuild.currentResult}: Job ${env.JOB_NAME}"		
        }
    }
}
