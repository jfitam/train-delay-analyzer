pipeline {
    agent any  
    
    tools {
		maven 'Maven3'
	}
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean install'  
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'        
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'target/**/*.jar', fingerprint: true
            junit 'target/surefire-reports/**/*.xml'
        }
    }
}