-- Création de la base de données (à exécuter manuellement avant ce script si nécessaire)
-- CREATE DATABASE taskmasterdb;

-- Connexion à la base de données (à faire dans psql ou via interface)
-- \c taskmasterdb

-- Supprimer les tables si elles existent déjà (pour éviter les conflits)

DROP TABLE IF EXISTS taskmaster_db.task_assignees, taskmaster_db.tasks, taskmaster_db.group_members, taskmaster_db.groups, taskmaster_db.users CASCADE;
-- Création de la table des utilisateurs
CREATE TABLE taskmaster_db.users (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(150) UNIQUE,
    telephone VARCHAR(20),
    address TEXT,
    password VARCHAR(100),
    role VARCHAR(20)
);

-- Création de la table des groupes
CREATE TABLE taskmaster_db.groups (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    date_creation DATE,
    admin_id INTEGER REFERENCES taskmaster_db.users(id) -- L'administrateur du groupe
);

-- Création de la table des membres des groupes (relation plusieurs-à-plusieurs)
CREATE TABLE taskmaster_db.group_members (
    group_id INTEGER REFERENCES taskmaster_db.groups(id),
    user_id INTEGER REFERENCES taskmaster_db.users(id),
    PRIMARY KEY (group_id, user_id)
);

-- Création de la table des tâches
CREATE TABLE taskmaster_db.tasks (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    due_date DATE,
    status VARCHAR(50),
    group_id INTEGER REFERENCES taskmaster_db.groups(id),
    admin_id INTEGER REFERENCES taskmaster_db.users(id) -- L'administrateur de la tâche
);

-- Création de la table d'affectation des tâches aux utilisateurs (plusieurs-à-plusieurs)
CREATE TABLE taskmaster_db.task_assignees (
    task_id INTEGER REFERENCES taskmaster_db.tasks(id),
    user_id INTEGER REFERENCES taskmaster_db.users(id),
    PRIMARY KEY (task_id, user_id)
);

-- Insertion des utilisateurs
INSERT INTO taskmaster_db.users (id, first_name, last_name, email, telephone, address, password, role) VALUES
(1, 'Keila', 'Roy', 'keilaroy26@gmail.com', '819-701-8694', '3400 Boul. Laviolette, Trois-Rivieres, QC', 'iloveMATHS', 'Admin'),
(2, 'Mac Roy', 'Simen', 'macroysimen@gmail.com', '819-448-6482', '8500 Damas, Yaounde, Centre', 'WhatIs5!', 'Admin'),
(3, 'Coco', 'Lyblassa', 'coco@gmail.com', '514-777-5678', '3000 Boul. Saint-Laurent, Montréal, QC', 'coco', 'Admin'),
(4, 'Camille', 'Lavoie', 'camille.lavoie@gmail.com', '514-777-5678', '3000 Boul. Saint-Laurent, Montréal, QC', 'cal', 'Membre'),
(5, 'Mathis', 'Gagnon', 'mathis@gmail.com', '581-432-7890', '85 Av. des Pins, Québec, QC', '123', 'Membre'),
(6, 'Yannick', 'Choundong', 'yannick.choundong@gmail.com', '581-245-1789', '4300 Avenue Gilles Boulet, Trois-Rivieres, QC', 'yan', 'Membre'),
(7, 'Astrid', 'Tchantchou', 'astrid.tchantchou@gmail.com', '819-224-6754', '710 Rue du Pere Marquette, Trois-Rivieres, QC', 'acetrend', 'Membre'),
(9, 'Albert', 'Einstein', 'albein@gmail.com', '4444456789' ,'112 Mercer Street, Berlin', '$2a$10$ZzpJZuQoTNDs/m8hZZdNT.XA07OoC/zxeqZDg6V975sIntsHyN38G', 'Admin'),
(10, 'Blanche', 'Simen', 'simenbla11@gmail.com', '418-556-8234', '8100 Rue Damas, Yaoundé, Cameroun', '$2a$10$Q2nXYGA0z1JMnWxi2Oq0MuFVij/PnfC1hZCbkzmLNaWU8Yrq0bbW.', 'Membre');


-- Insertion des groupes
INSERT INTO taskmaster_db.groups (id, name, date_creation, admin_id) VALUES
(1, 'Developpement Web', '2025-03-12', 3),
(2, 'Analyse des Données', '2025-03-12', 1),
(3, 'Sciences pures', '2025-03-12', 1),
(4, 'Mathematiques', '2025-03-12', 2),
(5, 'Environnement de développement et composantes logicielles', '2025-03-12', 2),
(6, 'groupe3', '2025-03-12', 3),
(7, 'groupe1', '2025-03-12', 3),
(8, 'groupe2', '2025-03-12', 3);

-- Insertion des membres dans les groupes
INSERT INTO taskmaster_db.group_members (group_id, user_id) VALUES
(1, 4), (1, 5),
(2, 5), (2, 6), (2, 7), (2, 10),
(3, 4), (3, 6), (3, 5),
(4, 7), (4, 4),
(5, 5), (5, 6), (5, 7),
(6, 5), (6, 6), (6, 7),
(7, 5), (7, 6), (7, 7),
(8, 5), (8, 6), (8, 7);

-- Insertion des tâches
INSERT INTO taskmaster_db.tasks (id, name, description, due_date, status, group_id, admin_id) VALUES
(1, 'Développer la page d''accueil', 'Créer la page principale du site web.', '2025-03-15', 'En attente', 1, 1),
(2, 'Analyse des données clients', 'Analyser les tendances des ventes et comportements clients.', '2025-03-20', 'Terminée', 2, 2),
(3, 'Corriger les bugs du projet', 'Réviser et corriger les erreurs de code signalées.', '2025-03-18', 'Non effectuée', 3, 3),
(4, 'Créer un modèle mathématique', 'Développer une équation de prédiction pour le projet de recherche.', '2025-04-01', 'En attente', 4, 2),
(5, 'Optimiser l''interface utilisateur', 'Améliorer l''expérience utilisateur sur l''application mobile.', '2025-03-25', 'En cours', 1, 1),
(6, 'Création du modèle supervisé', 'Utiliser les données par le test pour le modèle', '2025-04-10', 'En attente', 9, 9);

-- Insertion des membres assignés à chaque tâche
INSERT INTO taskmaster_db.task_assignees (task_id, user_id) VALUES
(1, 4), (1, 5),
(2, 6), (2, 7),
(3, 4), (3, 6), (3, 5),
(4, 7), (4, 4),
(5, 4), (5, 5),
(6,5), (6, 7), (6, 10);


SELECT MAX(id) FROM taskmaster_db.users;



ALTER SEQUENCE taskmaster_db.users_id_seq RESTART WITH 11;

ALTER SEQUENCE taskmaster_db.tasks_id_seq RESTART WITH 7;

ALTER SEQUENCE taskmaster_db.groups_id_seq RESTART WITH 9;
