pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('docker_Id')
        IMAGE_NAME = 'dinal1999/shopping-cart-app-v2'
        IMAGE_TAG = 'latest'
        DB_URL = credentials('db-url')
        DB_USER = credentials('db-user')
        DB_PASSWORD = credentials('db-password')
        SONAR_TOKEN = credentials('sonar-token')
    }

    tools {
        maven 'MAVEN_HOME'
        jdk 'JDK21'
    }

    stages {

        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo 'Building the project...'
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo 'Running unit tests...'
                bat 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('JaCoCo Coverage Report') {
            steps {
                echo 'Generating JaCoCo coverage report...'
                bat 'mvn jacoco:report'
            }
            post {
                always {
                    jacoco(
                            execPattern: 'target/jacoco.exec',
                            classPattern: 'target/classes',
                            sourcePattern: 'src/main/java'
                    )
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo 'Running SonarQube analysis...'
                withSonarQubeEnv('SonarQubeServer') {
                    bat """
                        mvn sonar:sonar ^
                        -Dsonar.projectKey=shopping-cart-week3 ^
                        -Dsonar.host.url=http://localhost:9000 ^
                        -Dsonar.token=%SONAR_TOKEN%
                    """
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo 'Building Docker image...'
                bat "docker build -t ${IMAGE_NAME}:${IMAGE_TAG} ."
            }
        }

        stage('Docker Push') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                bat """
                    echo %DOCKERHUB_CREDENTIALS_PSW% | docker login -u %DOCKERHUB_CREDENTIALS_USR% --password-stdin
                    docker push %IMAGE_NAME%:%IMAGE_TAG%
                """
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check the logs.'
        }
        always {
            bat 'docker logout'
        }
    }
}