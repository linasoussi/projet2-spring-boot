# Correspondance projet ↔ cours

| Élément du projet | Source dans le cours |
|---|---|
| Structure `model/repository/service/controller` | Ch.2 – slides 4, 12-14 (couches, structure d'un projet) |
| Dépendances Web, JPA, MySQL Driver, JDBC, H2 | Ch.2 – slides 11 et 15 |
| `application.properties` H2 (profil `h2`, tests) | Ch.2 – slides 16-19 |
| `application.properties` MySQL, `ddl-auto` | Ch.3 – slide 4 |
| `CREATE DATABASE projet2;` | Ch.3 – slide 3 |
| `@Entity`, `@Table`, `@Id`, `@GeneratedValue(IDENTITY)` | Ch.2 – slide 20 |
| `@Column(nullable, unique)` | Ch.3 – slide 5 |
| `@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`, `@JoinColumn`, `@JoinTable` | Ch.3 – slides 6, 9, 10 |
| `@Transient`, `@Enumerated`, `@Lob` | Ch.3 – slide 7 |
| `JpaRepository<Entity, Long>` | Ch.2 – slide 21 ; Ch.3 – slide 11 |
| `@Query` + `@Param` | Ch.3 – slide 12 |
| `@RestController`, `@RequestMapping`, `@Autowired`, `@GetMapping` | Ch.2 – slide 22 |
| `@PostMapping`, `@PutMapping`, `@RequestBody`, `@PathVariable` | Ch.2 – slide 23 |
| `@DeleteMapping` | Ch.2 – slide 24 |
| Endpoints `/api/...`, `/department/{name}`, affectation `{id}/department/{id}` | Ch.3 – slides 13-17 |
| Codes 200 / 201 / 400 / 404 / 500 | Ch.2 – slide 8 |
| Tests Postman GET / POST / PUT / DELETE | Ch.2 – slides 25-27 ; Ch.3 – slide 17 |
| Couche Métier (classes de services) | Ch.2 – slides 4 et 6 |
| `src/test/java` | Ch.2 – slide 14 |
| Exceptions (`orElseThrow`, exception levée si ressource absente) | Ch.1 – slide 14 ; Ch.2 – slide 22 |
| Collections `List`, `Set`, héritage/POO, getters/setters, constructeurs | Ch.1 |

## Éléments requis par le DS mais absents des slides
Le sujet du DS impose ces points ; ils ont été ajoutés de façon minimale et sont à bien comprendre :

| Élément | Où | Pourquoi |
|---|---|---|
| `spring-boot-starter-validation`, `@Valid`, `@NotBlank`, `@Email`, `@Min`… | `pom.xml`, `model/`, `controller/` | DS : « validation des données » |
| `@RestControllerAdvice` (`GlobalExceptionHandler`) | `exception/` | DS : « gestion des erreurs » + codes HTTP |
| `@Service`, `@Transactional` | `service/` | DS : package `service` (la couche métier est décrite Ch.2 slide 4) |
| `@ResponseStatus(HttpStatus.CREATED)` | `controller/` | Code 201 du Ch.2 slide 8 |
| `@JsonIgnore` sur les listes inverses | `model/` | Évite la boucle infinie JSON des relations bidirectionnelles |
| JUnit / `@DataJpaTest` / MockMvc | `src/test/` | Dossier `test/` de la structure attendue |
| Lombok | non utilisé | Optionnel dans le DS ; getters/setters écrits comme dans le cours |
