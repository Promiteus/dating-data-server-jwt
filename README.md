###Замечание по коллекциям
<p>Модель UserProfile и не только она, должны иметь поле id c аннотацией @Id - это автогенерируемое поле и 
чтобы не возникли проблемы при добавлении или изменении коллекции, его нельзя называть иначе, кроме как "id".</p>
<p>Также, если вы внесли изменения в коллекцию (по части id), то ее необходимо удалить полностью из mongo, в 
противном случае у вас будет ошибка дубликата ключа при изменении и добаления записи в коллекцию.</p>
<h4>Так выглядит изменяемая сущность:</h4>
<code>
 UserProfile(id=618f7ae9704fd01e6d02c7d8, firstName=Константин М, lastName=Матвеев, birthDate=Fri May 22 11:00:00 VLAST 1987, height=176, weight=65, aboutMe=Обо мне любая инфа, kids=0, familyStatus=SINGLE, rank=1400, sexOrientation=HETERO, sex=MAN)
</code>
<h4>Так выглядит id в базе:</h4>
<code>
id = ObjectId("618f7ae9704fd01e6d02c7d8")
<h4>Но мы при поиске запрашиваем только цифровую его часть:</h4>
<code>http://localhost:8090/api/user_profile/618f7ae9704fd01e6d02c7d8</code>


###Замечание по тестированию защищенных конечных точек
<p>Для того, чтобы отключить проверку аутентификации конечных точек во время тестирования, необходимо
переопределить конфигурацию безопасности на конфигурацию @TestConfiguration с такими же бинами, но
настройками, разрешающими любой доступ.</p>
<p>Новую конфигурацию <i>@TestConfiguration</i> необходмо внедрить в нужный класс теста через аннотацию <i>@Import()</i>
и обоим конфигурациям нужно присваивать раздельные профили. Также в файле application.properties надо добавить запись,
разрешающую переопределение бинов: <i>spring.main.allow-bean-definition-overriding=true</i></p>
<h3>Пример</h3>
<h4>Класс настроек безопасности</h4>
<pre>
@Configuration
@EnableReactiveMethodSecurity
@Profile(value = {"default", "prod"})
public class SecurityConfiguration {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserService userService;

    @Autowired
    public SecurityConfiguration(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userService = userService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) {
       return http
               .csrf().disable()
               .cors().disable()
               .httpBasic().disable()
               .formLogin().disable()
               .logout().disable()
               .addFilterAt(new JWTAuthorizationFilter(this.userService), SecurityWebFiltersOrder.AUTHENTICATION)
               .build();
    }

}
</pre>

<h4>Класс настроек безопасности для теста</h4>
<pre>
@TestConfiguration
@EnableReactiveMethodSecurity
public class TestSecurityConfiguration {
    private final UserService userService;

    @Autowired
    public TestSecurityConfiguration( UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, AuthenticationManager authenticationManager) {
        return http
                .csrf().disable()
                .cors().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .authorizeExchange().anyExchange().permitAll().and().build();
    }
}
</pre>

<h4>Класс теста - его аннотации</h4>
<pre>
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = {"test"})
@Import(value = TestSecurityConfiguration.class)
public class UserProfileControllerIntegration {}
</pre>

