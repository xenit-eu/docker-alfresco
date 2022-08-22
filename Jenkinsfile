pipeline {
    environment {
        ORG_GRADLE_PROJECT_buildDate = """${sh(
            returnStdout: true,
            script: 'date +%s',
        )}"""
    }
    agent any
    stages {
        stage("Unit Tests") {
            steps {
                sh "./gradlew -Penterprise test"
            }
        }
        stage("Build and integrationtest") {
            stages {
                stage("Build Docker Image") {
                    steps {
                        sh "./gradlew -Penterprise -Plegacy 3legacy:enterprise-6.2.2:buildDockerImage"
                    }
                }
                stage("Integration test") {
                    steps {
                        sh "./gradlew -Penterprise integrationTests --info"
                    }
                    post {
                        always {
                            sh "./gradlew -Penterprise composeDownAll"
                        }
                    }
                }
            }
        }

        stage("Publish Docker Image") {
            when {
                anyOf {
                    branch 'ethias'
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
