pipeline {
    agent any
    stages {
	stage("Build Docker Image") {
	    parallel {
		stage('Version 4') {
		    environment {
			VERSIONS_TO_BUILD = "4"
		    }
		    
		    steps {
			sh "./gradlew -Penterprise -Plegacy buildDockerImage"
		    }	    
		}
		
		stage('Version 5') {
		    environment {
			VERSIONS_TO_BUILD = "5"
		    }
		    steps {
			sh "./gradlew -Penterprise -Plegacy buildDockerImage"
		    }	    
		}
		
		stage('Version 6') {
		    environment {
			VERSIONS_TO_BUILD = "6"
		    }
		    steps {
			sh "./gradlew -Penterprise -Plegacy buildDockerImage"
		    }	    
		}
	    }
	}
		
	stage("Unit Tests") {
	    steps {
		sh "./gradlew -Penterprise test"
	    }
	}
	
	stage("Integration Tests") {
	    parallel {
		stage('Version 4') {
		    environment {
			VERSIONS_TO_BUILD = "4"
		    } 
		    steps {
			sh "./gradlew -Penterprise integrationTests --info"
		    }
		    post {
			always {
			    sh "./gradlew -Penterprise composeDownAll"
			}
		    }
		}
		stage('Version 5') {
		    environment {
			VERSIONS_TO_BUILD = "5"
		    } 
		    steps {
			sh "./gradlew -Penterprise integrationTests --info"
		    }
		    post {
			always {
			    sh "./gradlew -Penterprise composeDownAll"
			}
		    }
		}		
		stage('Version 6') {
		    environment {
			VERSIONS_TO_BUILD = "6"
		    } 
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
