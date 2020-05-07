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
            parallel {
                stage("Version 4") {
                    environment {
                        VERSIONS_TO_BUILD = "4"
                    }
                    stages {
                        stage("Build Docker Image") {
                            steps {
                                sh "./gradlew -Penterprise -Plegacy buildDockerImage"
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
                stage("Version 5") {
                    environment {
                        VERSIONS_TO_BUILD = "5"
                    }
                    stages {
                        stage("Build Docker Image") {
                            steps {
                                sh "./gradlew -Penterprise -Plegacy buildDockerImage"
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
                stage("Version 6") {
                    environment {
                        VERSIONS_TO_BUILD = "6"
                    }
                    stages {
                        stage("Build Docker Image") {
                            steps {
                                sh "./gradlew -Penterprise -Plegacy buildDockerImage"
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
