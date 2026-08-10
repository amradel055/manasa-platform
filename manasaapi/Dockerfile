FROM maven:3.6.3-jdk-8 AS builder

WORKDIR /build

COPY vendor-jars ./vendor-jars
RUN mvn install:install-file -Dfile=vendor-jars/arilFont-1.0.0.jar -DgroupId=com.isag.fonts.arialFont -DartifactId=arilFont -Dversion=1.0.0 -Dpackaging=jar && \
    mvn install:install-file -Dfile=vendor-jars/tafqeetArabic-1.0.0.jar -DgroupId=com.isag.tafqeet.arabic -DartifactId=tafqeetArabic -Dversion=1.0.0 -Dpackaging=jar && \
    mvn install:install-file -Dfile=vendor-jars/tafqeetEnglish-1.0.0.jar -DgroupId=com.isag.tafqeet.English -DartifactId=tafqeetEnglish -Dversion=1.0.0 -Dpackaging=jar && \
    mvn install:install-file -Dfile=vendor-jars/serenity-theme-2.1.1.jar -DgroupId=org.primefaces.themes -DartifactId=serenity-theme -Dversion=2.1.1 -Dpackaging=jar

COPY coremanasa ./coremanasa
RUN cd coremanasa && mvn -B -DskipTests install

COPY businessservicemanasa ./businessservicemanasa
RUN cd businessservicemanasa && mvn -B -DskipTests install

COPY apimanasa ./apimanasa
RUN cd apimanasa && mvn -B -DskipTests install

COPY igmamanasa ./igmamanasa
RUN cd igmamanasa && mvn -B -DskipTests install

RUN find /root/.m2/repository/mysql -name '*.jar' -exec cp {} /build/mysql-connector.jar \;

FROM payara/server-full:5.2022.5-jdk11

COPY --from=builder /build/igmamanasa/target/igmamanasa-1.0.war ${DEPLOY_DIR}/igmamanasa.war
COPY --from=builder /build/mysql-connector.jar ${PAYARA_DIR}/glassfish/domains/${DOMAIN_NAME}/lib/
COPY docker/postboot-commands.asadmin ${POSTBOOT_COMMANDS}
