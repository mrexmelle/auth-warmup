import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
	id("org.springframework.boot") version "3.0.5"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.7.22"
	id("org.jmailen.kotlinter") version "3.14.0"
}

group = "id.tanudjaja.authwarmup"
version = "0.1.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

springBoot {
	buildInfo()
}

dependencies {
	implementation("com.auth0:java-jwt:4.4.0")
	implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
	implementation("org.bouncycastle:bcpkix-jdk18on:1.73")
	implementation("org.bouncycastle:bcprov-jdk18on:1.73")
	implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.security:spring-security-crypto")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	runtimeOnly("org.postgresql:postgresql")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<BootRun> {
	systemProperties(System.getProperties().toMap() as Map<String,Object>)
}
