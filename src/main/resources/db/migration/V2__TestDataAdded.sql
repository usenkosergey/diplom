INSERT INTO users (is_moderator, reg_time, name, email, password)
VALUES
('true',1579956771856,'1Sergey', 'wert@dfre.cf', '454545'),
('true',1579956771856,'2Sergey', 'wert@dfre.cf', '454545'),
('false',1579956771856,'3Sergey', 'wert@dfre.cf', '454545');
---------------
INSERT INTO posts
(is_active, moderation_status, moderator_id, user_id,
time, title, text, view_count )
VALUES ('true', 'ACCEPTED', 1, 1, 1579956779856, 'ID-1 The canonical reference for building a production grade API with Spring.',
$$<h2 data-id=\"overview\"><strong>1. Overview</strong></h2>
 <div class=\"bd-anchor\" id=\"overview\"></div>
 <p><a href=\"https://www.baeldung.com/spring-boot\" rel=\"noopener noreferrer\">Spring Boot</a> and <a href=\"https://angular.io/\" rel=\"noopener noreferrer\">Angular</a> form a powerful tandem that works great for developing web applications with a minimal footprint.</p>
 <p>In this tutorial, <strong>we\'ll use <a href=\"https://www.baeldung.com/spring-boot\" rel=\"noopener noreferrer\">Spring Boot</a> for implementing a RESTful backend, and <a href=\"https://angular.io/\" rel=\"noopener noreferrer\">Angular</a> for creating a JavaScript-based frontend.</strong></p><div class=\"code-block code-block-9\" style=\"margin: 8px 0; clear: both;\">
 <span id=\"ezoic-pub-ad-placeholder-109\"></span><span style=\'display:block !important;float:none !important;margin-bottom:15px !important;margin-left:0px !important;margin-right:0px !important;margin-top:15px !important;min-height:400px;min-width:580px;text-align:center !important;\' class=\'ezoic-ad box-3 adtester-container adtester-container-109\' data-ez-name=\'baeldung_com-box-3\'><span id=\'div-gpt-ad-baeldung_com-box-3-0\' ezaw=\'580\' ezah=\'400\' style=\'position:relative;z-index:0;display:inline-block;min-height:400px;min-width:580px;\' class=\'ezoic-ad\'><script data-ezscrex=\'false\' data-cfasync=\'false\' type=\'text/javascript\' style=\'display:none;\'>eval(ez_write_tag([[580,400],\'baeldung_com-box-3\',\'ezslot_9\',109,\'0\',\'0\']));</script></span></span></div>
 <section class=\"further-reading-posts\"><div class=\"fr-header\"><h2>Further reading:</h2></div><div class=\"fr-wrap\"><div class=\"fr-item\"><h2><a href=\"https://www.baeldung.com/spring-interface-driven-controllers\">Interface Driven Controllers in Spring</a></h2><div class=\"ex\">Learn how to create controllers using Spring MVC request annotation on Java interfaces.</div><div class=\"ex\"><a href=\"https://www.baeldung.com/spring-interface-driven-controllers\">Read more</a> →</div></div><div class=\"fr-item\"><h2><a href=\"https://www.baeldung.com/rest-api-spring-oauth2-angular\">Spring REST API + OAuth2 + Angular</a></h2><div class=\"ex\">Learn how to set up OAuth2 for a Spring REST API and how to consume that from an Angular client.</div><div class=\"ex\"><a href=\"https://www.baeldung.com/rest-api-spring-oauth2-angular\">Read more</a> →</div></div></div></section>
 <h2 data-id=\"the-spring-boot-application\"><strong>2. The Spring Boot Application</strong></h2>
 <div class=\"bd-anchor\" id=\"the-spring-boot-application\"></div>
 <p>Our demo web application\'s functionality will be pretty simplistic indeed. It will be just narrowed to fetching and displaying a <em>List</em> of JPA entities from an&nbsp;in-memory <a href=\"https://www.baeldung.com/java-in-memory-databases\" rel=\"noopener noreferrer\">H2 database</a>, and persisting new ones through a plain HTML form.</p>
 <h3 data-id=\"1-the-maven-dependencies\"><strong>2.1. The Maven Dependencies</strong></h3>
 <div class=\"bd-anchor\" id=\"1-the-maven-dependencies\"></div>
 <p>Here are our Spring Boot project\'s dependencies:</p>$$, 1);

INSERT INTO posts
(is_active, moderation_status, moderator_id, user_id,
time, title, text, view_count )
VALUES ('true', 'ACCEPTED', 1, 1, 1579956979856, 'ID-2 Creating the Project',
$$<p>You can create the project using <a href=\"https://www.callicoder.com/scaffolding-your-spring-boot-application/#spring-boot-cli\">Spring Boot CLI</a> by typing the following command in your terminal -</p>
 <pre><code class=\"language-bash\">spring init -n=jpa-many-to-many-demo -d=web,jpa,mysql --package-name=com.example.jpa jpa-many-to-many-demo
 </code></pre>
 <p>If you don&rsquo;t have Spring Boot CLI installed, you can use the <a href=\"http://start.spring.io\">Spring Initializr</a> web tool to bootstrap the project by following the instructions below -</p>
 <ol>
 <li>Go to <a href=\"http://start.spring.io\">http://start.spring.io</a></li>
 <li>Click <strong>Switch to full version</strong> link to see all the options</li>
 <li>Enter <strong>Artifact</strong> as &ldquo;jpa-many-to-many-demo&rdquo;</li>
 <li>Change <strong>Package Name</strong> to &ldquo;com.example.jpa&rdquo;</li>
 <li>Select <strong>Web</strong>, <strong>JPA</strong> and <strong>Mysql</strong> dependencies.</li>
 <li>Click <strong>Generate Project</strong> to download the project.</li>
 </ol>$$, 2);

 INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'ACCEPTED', 1, 1, 1579156739856, 'ID-3 Configuring the Database and Hibernate Log levels',
 $$<p>Be sure to change the <code>spring.datasource.username</code> and <code>spring.datasource.password</code> properties as per your MySQL installation. Also, create a database named <code>jpa_many_to_many_demo</code>.</p>
  <p>The <code>spring.jpa.hibernate.ddl-auto = update</code> property makes sure that the database tables and the domain models in your application are in sync. Whenever you change the domain model, hibernate will automatically update the mapped table in the database when you restart the application.</p>
  <p>I have also specified the log levels for hibernate so that we can debug the SQL queries executed by hibernate.</p>
  <h2 id=\"defining-the-domain-models\">Defining the Domain models</h2>
  <p>Let&rsquo;s define the domain models which will be mapped to the tables we saw earlier. First, Create a package named <code>model</code> inside <code>com.example.jpa</code>, and then add the following classes inside the <code>model</code> package -</p>
  <p><strong>1. Post model</strong></p>$$, 3);

 INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'NEW', 1, 1, 1579956039856, 'ID-4 Скопируйте любой понравившийся статус и используйте его в ВК на своей стене.',
 $$<blockquote>Когда есть совесть - закон не нужен. </blockquote>
 <blockquote>— А… на что я тебе? - А на всю жизнь&#33; </blockquote>
 <blockquote>Работа свыше нам дана: замена счастию она. </blockquote>
 <blockquote>Мефобия — страх стать настолько офигенным, что все остальные не смогут совладать с таким великолепием и умрут. </blockquote>$$, 4);

  INSERT INTO posts
  (is_active, moderation_status, moderator_id, user_id,
  time, title, text, view_count )
  VALUES ('false', 'NEW', 1, 1, 1579956739856, 'ID-5 Правильно подобранный статус на стену в ВК.',
  $$<blockquote>Не льсти себе, ты можешь лучше </blockquote><blockquote>Не сахарная, от красивых слов не таю...
     </blockquote><blockquote>Лучше семь раз покрыться потом, чем один раз инеем!  </blockquote>$$, 5);

   INSERT INTO posts
   (is_active, moderation_status, moderator_id, user_id,
   time, title, text, view_count )
   VALUES ('true', 'NEW', 1, 1, 1579959939856, 'ID-6 что можно потавить на статус',
   $$<blockquote>Мужчина, как и машина, должен принадлежать только одной владелице.. Иначе это - общественный транспорт! </blockquote>
   <blockquote>смейся и весь мир будет смеяться вместе с тобой, плачь и ты будешь плакать в одиночестве </blockquote>$$, 6);
   
  

  
    INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'ACCEPTED', 1, 1, 1579156739856, 'ID-7 Configuring the Database and Hibernate Log levels',
 $$<p>Be sure to change the <code>spring.datasource.username</code> and <code>spring.datasource.password</code> properties as per your MySQL installation. Also, create a database named <code>jpa_many_to_many_demo</code>.</p>
  <p>The <code>spring.jpa.hibernate.ddl-auto = update</code> property makes sure that the database tables and the domain models in your application are in sync. Whenever you change the domain model, hibernate will automatically update the mapped table in the database when you restart the application.</p>
  <p>I have also specified the log levels for hibernate so that we can debug the SQL queries executed by hibernate.</p>
  <h2 id=\"defining-the-domain-models\">Defining the Domain models</h2>
  <p>Let&rsquo;s define the domain models which will be mapped to the tables we saw earlier. First, Create a package named <code>model</code> inside <code>com.example.jpa</code>, and then add the following classes inside the <code>model</code> package -</p>
  <p><strong>1. Post model</strong></p>$$, 7);
  
  
 INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'ACCEPTED', 1, 1, 1579156749856, 'ID-8 Configuring the Database and Hibernate Log levels',
 $$<p>Be sure to change the <code>spring.datasource.username</code> and <code>spring.datasource.password</code> properties as per your MySQL installation. Also, create a database named <code>jpa_many_to_many_demo</code>.</p>
  <p>The <code>spring.jpa.hibernate.ddl-auto = update</code> property makes sure that the database tables and the domain models in your application are in sync. Whenever you change the domain model, hibernate will automatically update the mapped table in the database when you restart the application.</p>
  <p>I have also specified the log levels for hibernate so that we can debug the SQL queries executed by hibernate.</p>
  <h2 id=\"defining-the-domain-models\">Defining the Domain models</h2>
  <p>Let&rsquo;s define the domain models which will be mapped to the tables we saw earlier. First, Create a package named <code>model</code> inside <code>com.example.jpa</code>, and then add the following classes inside the <code>model</code> package -</p>
  <p><strong>1. Post model</strong></p>$$, 8);

 INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'ACCEPTED', 1, 1, 1579156759856, 'ID-9 Configuring the Database and Hibernate Log levels',
 $$<p>Be sure to change the <code>spring.datasource.username</code> and <code>spring.datasource.password</code> properties as per your MySQL installation. Also, create a database named <code>jpa_many_to_many_demo</code>.</p>
  <p>The <code>spring.jpa.hibernate.ddl-auto = update</code> property makes sure that the database tables and the domain models in your application are in sync. Whenever you change the domain model, hibernate will automatically update the mapped table in the database when you restart the application.</p>
  <p>I have also specified the log levels for hibernate so that we can debug the SQL queries executed by hibernate.</p>
  <h2 id=\"defining-the-domain-models\">Defining the Domain models</h2>
  <p>Let&rsquo;s define the domain models which will be mapped to the tables we saw earlier. First, Create a package named <code>model</code> inside <code>com.example.jpa</code>, and then add the following classes inside the <code>model</code> package -</p>
  <p><strong>1. Post model</strong></p>$$, 9);

 INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'ACCEPTED', 1, 1, 1579156769856, 'ID-10 Configuring the Database and Hibernate Log levels',
 $$<p>Be sure to change the <code>spring.datasource.username</code> and <code>spring.datasource.password</code> properties as per your MySQL installation. Also, create a database named <code>jpa_many_to_many_demo</code>.</p>
  <p>The <code>spring.jpa.hibernate.ddl-auto = update</code> property makes sure that the database tables and the domain models in your application are in sync. Whenever you change the domain model, hibernate will automatically update the mapped table in the database when you restart the application.</p>
  <p>I have also specified the log levels for hibernate so that we can debug the SQL queries executed by hibernate.</p>
  <h2 id=\"defining-the-domain-models\">Defining the Domain models</h2>
  <p>Let&rsquo;s define the domain models which will be mapped to the tables we saw earlier. First, Create a package named <code>model</code> inside <code>com.example.jpa</code>, and then add the following classes inside the <code>model</code> package -</p>
  <p><strong>1. Post model</strong></p>$$, 10);

 INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'ACCEPTED', 1, 1, 1579156779856, 'ID-11 Configuring the Database and Hibernate Log levels',
 $$<p>Be sure to change the <code>spring.datasource.username</code> and <code>spring.datasource.password</code> properties as per your MySQL installation. Also, create a database named <code>jpa_many_to_many_demo</code>.</p>
  <p>The <code>spring.jpa.hibernate.ddl-auto = update</code> property makes sure that the database tables and the domain models in your application are in sync. Whenever you change the domain model, hibernate will automatically update the mapped table in the database when you restart the application.</p>
  <p>I have also specified the log levels for hibernate so that we can debug the SQL queries executed by hibernate.</p>
  <h2 id=\"defining-the-domain-models\">Defining the Domain models</h2>
  <p>Let&rsquo;s define the domain models which will be mapped to the tables we saw earlier. First, Create a package named <code>model</code> inside <code>com.example.jpa</code>, and then add the following classes inside the <code>model</code> package -</p>
  <p><strong>1. Post model</strong></p>$$, 11);

 INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'ACCEPTED', 1, 1, 1579156789856, 'ID-12 Configuring the Database and Hibernate Log levels',
 $$<p>Be sure to change the <code>spring.datasource.username</code> and <code>spring.datasource.password</code> properties as per your MySQL installation. Also, create a database named <code>jpa_many_to_many_demo</code>.</p>
  <p>The <code>spring.jpa.hibernate.ddl-auto = update</code> property makes sure that the database tables and the domain models in your application are in sync. Whenever you change the domain model, hibernate will automatically update the mapped table in the database when you restart the application.</p>
  <p>I have also specified the log levels for hibernate so that we can debug the SQL queries executed by hibernate.</p>
  <h2 id=\"defining-the-domain-models\">Defining the Domain models</h2>
  <p>Let&rsquo;s define the domain models which will be mapped to the tables we saw earlier. First, Create a package named <code>model</code> inside <code>com.example.jpa</code>, and then add the following classes inside the <code>model</code> package -</p>
  <p><strong>1. Post model</strong></p>$$, 12);

 INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'ACCEPTED', 1, 1, 1579156799856, 'ID-13 Configuring the Database and Hibernate Log levels',
 $$<p>Be sure to change the <code>spring.datasource.username</code> and <code>spring.datasource.password</code> properties as per your MySQL installation. Also, create a database named <code>jpa_many_to_many_demo</code>.</p>
  <p>The <code>spring.jpa.hibernate.ddl-auto = update</code> property makes sure that the database tables and the domain models in your application are in sync. Whenever you change the domain model, hibernate will automatically update the mapped table in the database when you restart the application.</p>
  <p>I have also specified the log levels for hibernate so that we can debug the SQL queries executed by hibernate.</p>
  <h2 id=\"defining-the-domain-models\">Defining the Domain models</h2>
  <p>Let&rsquo;s define the domain models which will be mapped to the tables we saw earlier. First, Create a package named <code>model</code> inside <code>com.example.jpa</code>, and then add the following classes inside the <code>model</code> package -</p>
  <p><strong>1. Post model</strong></p>$$, 13);

 INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'ACCEPTED', 1, 1, 1579156839856, 'ID-14 Configuring the Database and Hibernate Log levels',
 $$<p>Be sure to change the <code>spring.datasource.username</code> and <code>spring.datasource.password</code> properties as per your MySQL installation. Also, create a database named <code>jpa_many_to_many_demo</code>.</p>
  <p>The <code>spring.jpa.hibernate.ddl-auto = update</code> property makes sure that the database tables and the domain models in your application are in sync. Whenever you change the domain model, hibernate will automatically update the mapped table in the database when you restart the application.</p>
  <p>I have also specified the log levels for hibernate so that we can debug the SQL queries executed by hibernate.</p>
  <h2 id=\"defining-the-domain-models\">Defining the Domain models</h2>
  <p>Let&rsquo;s define the domain models which will be mapped to the tables we saw earlier. First, Create a package named <code>model</code> inside <code>com.example.jpa</code>, and then add the following classes inside the <code>model</code> package -</p>
  <p><strong>1. Post model</strong></p>$$, 14);

 INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'ACCEPTED', 1, 1, 1579156939856, 'ID-15 Configuring the Database and Hibernate Log levels',
 $$<p>Be sure to change the <code>spring.datasource.username</code> and <code>spring.datasource.password</code> properties as per your MySQL installation. Also, create a database named <code>jpa_many_to_many_demo</code>.</p>
  <p>The <code>spring.jpa.hibernate.ddl-auto = update</code> property makes sure that the database tables and the domain models in your application are in sync. Whenever you change the domain model, hibernate will automatically update the mapped table in the database when you restart the application.</p>
  <p>I have also specified the log levels for hibernate so that we can debug the SQL queries executed by hibernate.</p>
  <h2 id=\"defining-the-domain-models\">Defining the Domain models</h2>
  <p>Let&rsquo;s define the domain models which will be mapped to the tables we saw earlier. First, Create a package named <code>model</code> inside <code>com.example.jpa</code>, and then add the following classes inside the <code>model</code> package -</p>
  <p><strong>1. Post model</strong></p>$$, 15);

 INSERT INTO posts
 (is_active, moderation_status, moderator_id, user_id,
 time, title, text, view_count )
 VALUES ('true', 'ACCEPTED', 1, 1, 1579157739856, 'ID-16 Configuring the Database and Hibernate Log levels',
 $$<p>Be sure to change the <code>spring.datasource.username</code> and <code>spring.datasource.password</code> properties as per your MySQL installation. Also, create a database named <code>jpa_many_to_many_demo</code>.</p>
  <p>The <code>spring.jpa.hibernate.ddl-auto = update</code> property makes sure that the database tables and the domain models in your application are in sync. Whenever you change the domain model, hibernate will automatically update the mapped table in the database when you restart the application.</p>
  <p>I have also specified the log levels for hibernate so that we can debug the SQL queries executed by hibernate.</p>
  <h2 id=\"defining-the-domain-models\">Defining the Domain models</h2>
  <p>Let&rsquo;s define the domain models which will be mapped to the tables we saw earlier. First, Create a package named <code>model</code> inside <code>com.example.jpa</code>, and then add the following classes inside the <code>model</code> package -</p>
  <p><strong>1. Post model</strong></p>$$, 16);
  

----------------
Insert Into tags (name) VALUES ('Tag_1');
Insert Into tags (name) VALUES ('Tag_2');
Insert Into tags (name) VALUES ('Tag_3');
Insert Into tags (name) VALUES ('Tag_4');
Insert Into tags (name) VALUES ('Tag_5');
------------------------
Insert Into tag2post (post_id, tag_id) VALUES (1,1);
Insert Into tag2post (post_id, tag_id) VALUES (1,2);
Insert Into tag2post (post_id, tag_id) VALUES (2,3);
Insert Into tag2post (post_id, tag_id) VALUES (2,4);
Insert Into tag2post (post_id, tag_id) VALUES (3,1);
Insert Into tag2post (post_id, tag_id) VALUES (3,5);
Insert Into tag2post (post_id, tag_id) VALUES (4,5);
Insert Into tag2post (post_id, tag_id) VALUES (4,2);
Insert Into tag2post (post_id, tag_id) VALUES (5,5);
Insert Into tag2post (post_id, tag_id) VALUES (5,1);
Insert Into tag2post (post_id, tag_id) VALUES (6,2);

-----------------
Insert Into global_settings
(code, name, value)
VALUES
('MULTIUSER_MODE', 'Многопользовательский режим', 'true'),
('POST_PREMODERATION', 'Премодерация постов', 'false'),
('STATISTICS_IS_PUBLIC', 'Показывать всем статистику блога', 'true');
-----------------
Insert Into post_votes
(user_id, post_id, time, value)
VALUES
(1,1,1579157739856,1),
(1,2,1579157739856,1),
(1,3,1579157739856,-1),
(1,4,1579157739856,-1),
(1,5,1579157739856,-1),
(2,3,1579157739856,1),
(2,7,1579157739856,-1),
(2,2,1579157739856,1),
(2,3,1579157739856,-1),
(2,2,1579157739856,-1),
(5,6,1579157739856,-1),
(4,6,1579157739856,-1),
(3,6,1579157739856,-1),
(2,6,1579157739856,-1),
(1,6,1579157739856,-1),
(1,9,1579157739856,1),
(2,9,1579157739856,1),
(3,9,1579157739856,1),
(4,9,1579157739856,1),
(5,9,1579157739856,1),
(3,3,1579157739856,-1);
--------------------------
Insert Into post_comments
(parent_id, post_id, user_id, text, time)
VALUES
(0, 3, 1, 'К1 к посту 3', 1583250619160),
(0, 7, 1, 'К2 к посту 7', 1583250619160),
(2, 7, 1, 'К3 к ком К2 к посту 7', 1583250619160),
(0, 7, 1, 'К7 к посту 7', 1583250639160),
(0, 3, 1, 'К4 к посту 3', 1583250619160),
(0, 3, 1, 'К6 к посту 3', 1583250629160),
(0, 2, 1, 'К5 к посту 2', 1583250619160);