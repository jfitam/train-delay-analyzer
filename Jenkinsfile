pipeline {
    agent any
    
    tools {
      maven 'Maven3'
	}
	
    stages {
        stage('Build') { steps { sh 'mvn clean install' } }
        stage('Test') { steps { sh 'mvn test' } }
        stage('Package') { steps { sh 'mvn package' } }
        stage('Manual Approval') {
            steps {
                input message: '¿Proceed with deployment?', ok: 'Yes, deploy'
            }
        }

        stage('Publish / Deploy') {
            steps {
                echo 'deployment not setup'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            junit 'target/surefire-reports/**/*.xml'
        }
    }
}
