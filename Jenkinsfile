node {
   
   stage('Build Maven') {
        sh "mvn clean package"
   }
   
   stage('Docker deploy') {
		sh "docker build -t sca-api -f Dockerfile ."
        sh "docker run --name sca-api-desenv --network outerboxtech_network -d -p 8090:8090 sca-api"
   }

}