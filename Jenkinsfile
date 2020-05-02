pipeline {
    agent any
    stages {
        stage ('Build') {
            steps {
                withMaven(maven : 'Maven') {
                    sh 'mvn clean compile'
                }
            }
        }
        stage ('Test') {
            steps {
                withMaven(maven : 'Maven') {
                    sh 'mvn test'
                }
            }
        }
        stage ('Deploy') {
			environment {
                PROJETO_GIT = env.GIT_URL.replaceAll('https://github.com/fabricaOuterbox/', '').replaceAll('.git', '')
            }
            steps {
                withMaven(maven : 'Maven') {
                    sh 'mvn install'
                }
				
				sh 'mkdir -p /home/projetos/$PROJETO_GIT/deploy'
				
				sh 'cp "/var/lib/jenkins/workspace/$PROJETO_GIT/target/$PROJETO_GIT.jar" "/home/projetos/$PROJETO_GIT/deploy"'

                sh "ps -ef | grep '\$PROJETO_GIT'| grep -v grep | awk '{ print \$2 }' | xargs --no-run-if-empty kill"

				sh 'JENKINS_NODE_COOKIE=dontKillMe_$PROJETO_GIT nohup java -jar -Dspring.profiles.active=oauth-security,prod /home/projetos/$PROJETO_GIT/deploy/$PROJETO_GIT.jar >  /home/projetos/$PROJETO_GIT/deploy/server-prod.log 2>&1 &'
            }
        }
    }
}