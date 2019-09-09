pipeline {
    agent any
    stages {
	parallel {
	    stage('Version 4') {
		environment {
		    VERSIONS_TO_BUILD = "4"
		}
		
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
	    
	    stage('Version 5') {
		environment {
		    VERSIONS_TO_BUILD = "5"
		}
		
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

	    stage('Version 6') {
		environment {
		    VERSIONS_TO_BUILD = "6"
		}
		
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
	}
    }
}
