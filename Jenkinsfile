pipeline {
    agent any

    environment {
        DOCKERHUB_USER  = "tokiandjack"
        SHOP_IMAGE      = "${DOCKERHUB_USER}/coffee-shop"
        ACCOUNT_IMAGE   = "${DOCKERHUB_USER}/coffee-account"
        DOCKER_TAG      = "${BUILD_NUMBER}"
        CREDS_ID        = "dockerhub-creds"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build coffee-shop JAR') {
            steps {
                dir('coffee-shop') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build coffee-account JAR') {
            steps {
                dir('coffee-account/coffeeaccount') {
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat "docker build -t %SHOP_IMAGE%:%DOCKER_TAG% ./coffee-shop"
                bat "docker build -t %ACCOUNT_IMAGE%:%DOCKER_TAG% ./coffee-account/coffeeaccount"
                bat "docker tag %SHOP_IMAGE%:%DOCKER_TAG% %SHOP_IMAGE%:latest"
                bat "docker tag %ACCOUNT_IMAGE%:%DOCKER_TAG% %ACCOUNT_IMAGE%:latest"
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(
                    credentialsId: "${CREDS_ID}",
                    usernameVariable: 'DOCKER_USER',
                    passwordVariable: 'DOCKER_PASS')]) {

                    bat 'echo %DOCKER_PASS%| docker login -u %DOCKER_USER% --password-stdin'
                    bat "docker push %SHOP_IMAGE%:%DOCKER_TAG%"
                    bat "docker push %SHOP_IMAGE%:latest"
                    bat "docker push %ACCOUNT_IMAGE%:%DOCKER_TAG%"
                    bat "docker push %ACCOUNT_IMAGE%:latest"
                    bat 'docker logout'
                }
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                bat 'docker-compose down || echo nothing to stop'
                bat 'docker-compose up -d --build'
                bat 'ping -n 16 127.0.0.1 > nul'
                bat 'docker ps'
            }
        }
    }

    post {
        success {
            echo "BUILD ${BUILD_NUMBER} SUCCESS"
            echo "tokiandjack/coffee-shop:${BUILD_NUMBER} pushed"
            echo "tokiandjack/coffee-account:${BUILD_NUMBER} pushed"
        }
        failure {
            echo "BUILD ${BUILD_NUMBER} FAILED"
        }
        always {
            bat 'docker image prune -f || echo done'
        }
    }
}
