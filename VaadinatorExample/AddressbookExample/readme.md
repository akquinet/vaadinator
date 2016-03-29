# Vaadinator Addressbook Example
## Running
This example has different profiles which have to be selected:
- desktop or touchkit
- tomcat (H2), tomcatex (Postgres) or jboss (Postgres)

To run the tomcat desktop variant call `mvn jetty:run -Pdesktop,tomcat,stageres`, then go with your webbrowser to http://localhost:8080. (stageres is required to include all Vaadin Files into Jetty)

For the touchkit tomcat variant you first have to build the widgetset with `mvn vaadin:compile -Ptouchkit,tomcat`, then launch the war with `mvn jett:run-war -Ptouchkit,tomcat,stageres`.
