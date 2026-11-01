# Présentation (5 minutes) – plan conseillé

| Temps | Qui | Contenu |
|---|---|---|
| 0:00 – 0:30 | M3 | Contexte métier : gestion universitaire ; pourquoi ce choix (extension du cas User/Department du cours) |
| 0:30 – 1:30 | M1 | Architecture en couches (Controller → Service → Repository → MySQL), diagramme de classes, 7 entités et relations (OneToMany, ManyToOne, OneToOne, ManyToMany) |
| 1:30 – 2:30 | M1 | Configuration MySQL (`ddl-auto=update`), requêtes personnalisées (`@Query`, `@Param`) |
| 2:30 – 3:30 | M2 | Règles métier (inscription, notes), validation, gestion des erreurs et codes HTTP (200/201/400/404/500) |
| 3:30 – 4:30 | M3 | **Démo Postman** : création → requête personnalisée → cas d'erreur 400 / 404 |
| 4:30 – 5:00 | M3 | Conclusion, tests JUnit, difficultés rencontrées, améliorations possibles |

Conseils : une démo live de 1 minute vaut mieux que 5 slides ; préparer des données déjà saisies ; chaque membre doit être prêt à répondre sur tout le projet.
