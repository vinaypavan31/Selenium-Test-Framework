pipeline {

    agent any

    tools {
        maven 'maven-3.9.12'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/vinaypavan31/Selenium-Test-Framework.git'
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Reports') {
            steps {
                publishHTML(target: [
                    reportDir: 'src/test/resources/ExtentReports',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Spark Report'
                ])
            }
        }
    }

    post {

        always {
            archiveArtifacts artifacts: '**/src/test/resources/ExtentReports/*.html',
                             fingerprint: true

            junit 'target/surefire-reports/*.xml'
        }

        success {
            emailext(
                to: 'vinaypavan14264@gmail.com',
                subject: "Build Success: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                attachLog: true,
                body: """
                <html>
                <body>
                <p>Hello Team,</p>

                <p>The latest Jenkins build has completed successfully.</p>

                <p><b>Project Name:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>
                <p><b>Status:</b> <span style="color:green;"><b>SUCCESS</b></span></p>
                <p><b>Build URL:</b>
                   <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>

                <p><b>Commit:</b> ${env.GIT_COMMIT}</p>
                <p><b>Branch:</b> ${env.GIT_BRANCH}</p>

                <p><b>Extent Report:</b>
                   <a href="${env.BUILD_URL}HTML_20Extent_20Report/">Click here</a></p>

                <p>Best regards,<br><b>Automation Team</b></p>
                </body>
                </html>
                """
            )
        }

        failure {
            emailext(
                to: 'vinaypavan14264@gmail.com',
                subject: "Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                mimeType: 'text/html',
                attachLog: true,
                body: """
                <html>
                <body>
                <p>Hello Team,</p>

                <p>The Jenkins build has
                   <span style="color:red;"><b>FAILED</b></span>.</p>

                <p><b>Project:</b> ${env.JOB_NAME}</p>
                <p><b>Build Number:</b> #${env.BUILD_NUMBER}</p>

                <p><b>Build URL:</b>
                   <a href="${env.BUILD_URL}">${env.BUILD_URL}</a></p>

                <p><b>Commit:</b> ${env.GIT_COMMIT}</p>
                <p><b>Branch:</b> ${env.GIT_BRANCH}</p>

                <p>Please check logs and take action.</p>

                <p><b>Extent Report (if available):</b>
                   <a href="${env.BUILD_URL}HTML_20Extent_20Report/">Click here</a></p>

                <p>Regards,<br><b>Automation Team</b></p>
                </body>
                </html>
                """
            )
        }
    }
}
