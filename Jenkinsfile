pipeline {
    agent any
    stages {
        stage('Build') {
			steps {
				echo 'Building...'
				mkdir -p '/home/deploy'
				
				echo 'Update branch...'
				if [ -d /home/deploy/obt-sca-api ]; then
					cd '/home/deploy/obt-sca-api'
					git pull
				else
				   cd '/home/deploy'
				   git clone https://github.com/fabricaOuterbox/obt-sca-api.git
				   cd '/home/deploy/obt-sca-api'
				fi
				
				mvn clean package
            }
        }
        stage('Test') {
            steps {
                echo 'Testing...'
            }
        }
		stage('Deploy') {
            steps {
                echo 'Deploy...'
            }
        }
    }
}