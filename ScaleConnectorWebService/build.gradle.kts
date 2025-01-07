plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.tomcat:tomcat-catalina:10.1.0") // Adjust version as needed
    implementation("org.apache.tomcat:tomcat-jasper:10.1.0")   // For JSP support
    implementation("org.apache.tomcat.embed:tomcat-embed-core:10.1.0") // Embedded Tomcat support
    implementation("javax.servlet:javax.servlet-api:4.0.1")
    compileOnly("javax.servlet:javax.servlet-api:4.0.1")


}



tasks.test {
    useJUnitPlatform()
}