# Rôles et tâches de l'équipe (3 membres)

**Principe de répartition** : chaque membre est propriétaire d'un **module vertical** (entités → repository → service → controller) **et** d'une **responsabilité transversale**. Ainsi chacun maîtrise une application complète de bout en bout, ce qui est exigé à l'oral du DS (« chaque étudiant doit maîtriser le code qu'il présente »).

| | Membre 1 | Membre 2 | Membre 3 |
|---|---|---|---|
| **Rôle** | Chef de projet · Architecte base de données | Développeur Métier · Qualité & Erreurs | Intégrateur API · Tests Postman · Documentation |
| **Module** | Structure académique | Étudiants | Évaluations |
| **Entités** | Department, Teacher, Course | Student, StudentProfile, StudentStatus | Exam, Grade |
| **Transversal** | MySQL, configuration, GitHub | Exceptions, validation, tests JUnit | Postman, README, présentation |

---

## Membre 1 – Chef de projet & Architecte base de données

### Responsabilité transversale (Chapitres 2 et 3)
- [ ] Installer MySQL et créer la base : `CREATE DATABASE projet2;` (Ch.3, slide 3) → `database/init.sql`
- [ ] Générer le projet avec Spring Initializr / Spring Tools avec les dépendances du cours : Spring Web, Spring Data JPA, MySQL Driver, Spring Data JDBC, H2 (Ch.2, slides 10-11, 15) → `pom.xml`
- [ ] Respecter la structure `model / repository / service / controller` + `Projet2Application` (Ch.2, slides 12-14)
- [ ] Configurer `application.properties` pour MySQL avec `ddl-auto=update` (Ch.3, slide 4) et le profil H2 `application-h2.properties` (Ch.2, slides 16-19)
- [ ] Créer le dépôt GitHub, le `.gitignore`, les branches (`main`, `dev`, `membre1-…`) et **ajouter l'enseignant comme collaborateur : awadi.hatem@gmail.com**
- [ ] Relire et fusionner les Pull Requests ; garantir que `main` compile

### Module « Structure académique »
- [ ] Entités : `Department` (`@OneToMany`), `Teacher` (`@ManyToOne`), `Course` (`@ManyToOne` ×2, `@ManyToMany(mappedBy)`, `@OneToMany`) – Ch.3, slides 5-10
- [ ] Repositories : `DepartmentRepository`, `TeacherRepository`, `CourseRepository`
- [ ] Requêtes personnalisées : `findTeachersByDepartmentName`, `findCoursesByStudentId`, `findPopularCourses` (Ch.3, slide 12)
- [ ] Services : `DepartmentService`, `TeacherService`, `CourseService` (unicité, blocage des suppressions)
- [ ] Controllers : `DepartmentController`, `TeacherController`, `CourseController` (Ch.3, slides 13-14)

### Questions probables à l'oral
Pourquoi `ddl-auto=update` ? Différence entre `none`, `update`, `create`, `create-drop` ? Rôle de `mappedBy` ? Pourquoi `@JoinColumn(name="department_id")` ? Comment fonctionne `@Query` avec `@Param` ?

**Barème visé** : Cohérence du domaine (2) · Repository (3) · Qualité du code (2)

---

## Membre 2 – Développeur Métier, Gestion des erreurs & Tests

### Responsabilité transversale
- [ ] Package `exception` : `ResourceNotFoundException` (404), `GlobalExceptionHandler` (400 / 404 / 500 – codes du Ch.2, slide 8)
- [ ] Contrôler les **validations** de toutes les entités (`@NotBlank`, `@Email`, `@Min`, `@Max`, `@Positive`, `@NotNull`) et `@Valid` dans les contrôleurs
- [ ] Écrire les tests JUnit dans `src/test/java` (Ch.2, slide 14) : `RepositoryTests`, `ServiceTests`, `ControllerTests` (H2 en mémoire) et vérifier `mvn test` au vert

### Module « Étudiants »
- [ ] Entités : `Student` (`@Enumerated`, `@OneToOne` + `@JoinColumn`, `@ManyToMany` + `@JoinTable`, `@ManyToOne`), `StudentProfile` (`@Lob`), enum `StudentStatus` – Ch.3, slides 5-7
- [ ] Repository : `StudentRepository` avec `findStudentByDepartmentName` (identique au Ch.3, slide 12) et `findByStatus`
- [ ] Service : `StudentService` – création/mise à jour, affectation à un département (Ch.3, slide 16), inscription / désinscription à un cours (règle : pas de doublon), suppression protégée
- [ ] Controller : `StudentController` (CRUD, `/department/{name}`, `/status/{status}`, `/{studentId}/department/{deptId}`, `/{studentId}/courses/{courseId}`)

### Questions probables à l'oral
Rôle de `@OneToOne` vs `@ManyToMany` ? Que fait `@JoinTable` ? `EnumType.STRING` vs `ORDINAL` ? Quel code HTTP pour quelle erreur ? Pourquoi `@Valid` ? Où est la logique métier (couche Service) ?

**Barème visé** : Modélisation JPA (4) · Service (2) · Qualité du code (2)

---

## Membre 3 – Intégrateur API, Tests Postman, Documentation & Présentation

### Responsabilité transversale
- [ ] Importer et exécuter `postman/Projet2.postman_collection.json` (Ch.2, slides 25-27), l'enrichir si besoin, **exporter la collection**
- [ ] Prendre les **captures d'écran Postman** des tests réussis (dossier `screenshots/`) – cas de succès **et** d'erreur (400, 404)
- [ ] Finaliser le `README.md` (contexte, entités, endpoints, instructions d'exécution)
- [ ] Préparer la **présentation de 5 minutes** (`docs/PRESENTATION_5MIN.md`) et organiser la répétition de l'équipe
- [ ] Vérifier la check-list finale avant livraison (voir plus bas)

### Module « Évaluations »
- [ ] Entités : `Exam` (`@ManyToOne` vers Course, `LocalDate`), `Grade` (`@ManyToOne` ×2, `@Transient` mention calculée, contrainte d'unicité étudiant+examen)
- [ ] Repositories : `ExamRepository`, `GradeRepository` avec la requête `findAverageOn20ByStudentId` (AVG)
- [ ] Services : `ExamService`, `GradeService` (note ≤ barème, étudiant inscrit au cours, pas de doublon)
- [ ] Controllers : `ExamController`, `GradeController` + endpoint `GET /api/students/{id}/average` (coordination avec le Membre 2)

### Questions probables à l'oral
Comment `@Transient` fonctionne ? Que fait la requête AVG ? Comment tester un cas d'erreur dans Postman ? Pourquoi 201 pour un POST ? Rôle de `@RequestBody` et `@PathVariable` ?

**Barème visé** : Controller (3) · Tests Postman (2) · Présentation (2)

---

## Planning conseillé

| Étape | Qui | Contenu |
|---|---|---|
| 1 – Mise en place | M1 (avec M2 et M3) | MySQL, Initializr, GitHub, `application.properties`, partage du dépôt |
| 2 – Persistance | M1, M2, M3 en parallèle | Chacun crée ses entités + repositories, vérifie les tables dans MySQL |
| 3 – Métier + API | M1, M2, M3 en parallèle | Services puis controllers de chaque module |
| 4 – Intégration | M1 | Fusion des branches sur `dev`, démarrage complet de l'application |
| 5 – Qualité | M2 puis M3 | Tests JUnit, collection Postman, captures d'écran |
| 6 – Livraison | M3 (tous relisent) | README, présentation, répétition, check-list finale |

## Règles de travail en équipe
- Une branche par membre (`membre1-structure`, `membre2-etudiants`, `membre3-evaluations`), Pull Request vers `dev`, revue par un autre membre.
- Messages de commit clairs (ex. `feat(student): ajout inscription à un cours`).
- Chaque membre **doit pouvoir expliquer l'intégralité du projet** (annotations, relations, méthodes principales), même la partie des autres.

## Check-list finale (avant la remise)
- [ ] Le dépôt GitHub contient tout le code ; **awadi.hatem@gmail.com** est ajouté comme collaborateur
- [ ] `README.md` complet (contexte, entités, endpoints, exécution)
- [ ] L'application démarre avec MySQL ; les tables sont créées
- [ ] `mvn test` au vert
- [ ] Collection Postman exportée + captures d'écran dans `screenshots/`
- [ ] Présentation de 5 minutes prête et répétée
