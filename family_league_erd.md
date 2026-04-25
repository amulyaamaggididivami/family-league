# Family League — Entity Relationship Diagram

```mermaid
erDiagram

  users {
    uuid id PK
    string username UK
    string email UK
    string password_hash
    string full_name
    string avatar_url
    boolean is_active
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  roles {
    uuid id PK
    string name UK
    string description
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  user_roles {
    uuid id PK
    uuid user_id FK
    uuid role_id FK
    boolean is_active
    timestamp assigned_at
    uuid assigned_by FK
    timestamp deleted_at
  }

  league_types {
    uuid id PK
    string name UK
    string short_code UK
    string country
    string gender
    string format
    string governing_body
    string description
    boolean is_active
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  leagues {
    uuid id PK
    uuid league_type_id FK
    string name
    int season_year
    string status
    string description
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  league_seasons {
    uuid id PK
    uuid league_id FK
    string season_name
    string status
    timestamp league_prediction_lock_time
    timestamp start_time
    timestamp end_time
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  league_season_members {
    uuid id PK
    uuid season_id FK
    uuid user_id FK
    string status
    timestamp joined_at
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  teams {
    uuid id PK
    string name
    string short_code UK
    string logo_url
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  players {
    uuid id PK
    uuid team_id FK
    string name
    string position
    boolean is_active
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  season_teams {
    uuid id PK
    uuid season_id FK
    uuid team_id FK
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  matches {
    uuid id PK
    uuid season_id FK
    uuid home_team_id FK
    uuid away_team_id FK
    timestamp scheduled_at
    timestamp prediction_lock_time
    string venue
    string status
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  match_results {
    uuid id PK
    uuid match_id FK
    uuid winning_team_id FK
    uuid toss_winner_team_id FK
    uuid player_of_match_id FK
    boolean is_tie
    timestamp published_at
    uuid published_by FK
    timestamp verified_at
    uuid verified_by FK
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  league_predictions {
    uuid id PK
    uuid season_id FK
    uuid user_id FK
    timestamp submitted_at
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  league_team_predictions {
    uuid id PK
    uuid league_prediction_id FK
    uuid team_id FK
    int predicted_rank
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  match_predictions {
    uuid id PK
    uuid match_id FK
    uuid user_id FK
    uuid predicted_winner_id FK
    uuid predicted_toss_winner_id FK
    uuid predicted_player_of_match_id FK
    timestamp submitted_at
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  prediction_points {
    uuid id PK
    uuid user_id FK
    uuid season_id FK
    uuid match_id FK
    uuid match_prediction_id FK
    uuid league_prediction_id FK
    int points_earned
    string point_type
    string source_field
    timestamp calculated_at
    timestamp created_at
  }

  leaderboard_snapshots {
    uuid id PK
    uuid season_id FK
    uuid user_id FK
    int total_points
    int rank
    timestamp snapshot_at
    timestamp created_at
  }

  league_results {
    uuid id PK
    uuid season_id FK
    uuid first_place_team_id FK
    uuid second_place_team_id FK
    uuid third_place_team_id FK
    timestamp published_at
    uuid published_by FK
    timestamp verified_at
    uuid verified_by FK
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  bulk_communication_campaigns {
    uuid id PK
    uuid admin_id FK
    string event_type
    string message_template
    jsonb user_filter
    int recipients_count
    timestamp sent_at
    timestamp created_at
    uuid created_by FK
  }

  email_logs {
    uuid id PK
    uuid user_id FK
    uuid campaign_id FK
    string event_type
    string subject
    string body_ref
    string status
    string error_message
    timestamp sent_at
    timestamp scheduled_at
    timestamp created_at
    uuid created_by FK
    timestamp updated_at
    uuid updated_by FK
    timestamp deleted_at
  }

  audit_logs {
    uuid id PK
    string entity_type
    uuid entity_id
    string action
    uuid actor_id FK
    jsonb old_value
    jsonb new_value
    string ip_address
    timestamp created_at
  }

  %% Auth & Roles
  users ||--o{ user_roles : has
  roles ||--o{ user_roles : assigned_via

  %% League hierarchy
  league_types ||--o{ leagues : categorises
  leagues      ||--o{ league_seasons : has

  %% Season membership & teams
  league_seasons ||--o{ league_season_members : has
  users          ||--o{ league_season_members : joins
  league_seasons ||--o{ season_teams : includes
  teams          ||--o{ season_teams : participates
  teams          ||--o{ players : has

  %% Matches
  league_seasons ||--o{ matches : schedules
  teams          ||--o{ matches : plays_home
  teams          ||--o{ matches : plays_away
  matches        ||--o| match_results : has

  %% Match result FK references (explicit)
  teams   |o--o{ match_results : wins
  teams   ||--o{ match_results : wins_toss
  players ||--o{ match_results : player_of_match

  %% Predictions
  users              ||--o{ match_predictions : makes
  matches            ||--o{ match_predictions : receives
  teams              |o--o{ match_predictions : predicted_winner
  teams              |o--o{ match_predictions : predicted_toss_winner
  players            |o--o{ match_predictions : predicted_potm
  users              ||--o{ league_predictions : makes
  league_seasons     ||--o{ league_predictions : has
  league_predictions ||--o{ league_team_predictions : contains

  %% Points & Leaderboard
  users              ||--o{ prediction_points : earns
  match_predictions  ||--o{ prediction_points : generates
  league_predictions ||--o{ prediction_points : generates
  league_seasons     ||--o{ leaderboard_snapshots : tracks
  users              ||--o{ leaderboard_snapshots : has

  %% League final result
  league_seasons ||--o| league_results : produces
  teams          |o--o{ league_results : first_place
  teams          |o--o{ league_results : second_place
  teams          |o--o{ league_results : third_place

  %% Operational
  users                        ||--o{ email_logs : receives
  bulk_communication_campaigns |o--o{ email_logs : generates
  users                        ||--o{ bulk_communication_campaigns : creates
  users                        ||--o{ audit_logs : performs
```

---

## Table Summary

| Table | Purpose |
|---|---|
| `users` | All platform users (admin and regular) |
| `roles` | Master role definitions — `ROLE_USER`, `ROLE_ADMIN` |
| `user_roles` | Many-to-many join; an admin holds both roles |
| `league_types` | Tournament master — IPL, WPL, BBL with format/gender/country |
| `leagues` | A specific edition of a league type — e.g. IPL 2025 |
| `league_seasons` | Phase within an edition; holds `league_prediction_lock_time` (4 hrs before first match) |
| `league_season_members` | Users participating in a given season |
| `teams` | Team master, reusable across seasons |
| `players` | Players belonging to a team |
| `season_teams` | Teams registered for a specific season |
| `matches` | Scheduled matches within a season; holds per-match `prediction_lock_time` (1 hr before start) |
| `match_results` | Admin-entered and admin-verified result for a match |
| `league_predictions` | A user's league-level prediction for a season |
| `league_team_predictions` | Full rank ordering (1 to n) within a league prediction |
| `match_predictions` | A user's per-match prediction (winner, toss, POTM) |
| `prediction_points` | Immutable points ledger, calculated server-side only |
| `leaderboard_snapshots` | Ranked snapshot after each result is processed |
| `league_results` | Admin-entered and admin-verified final standings for a season |
| `bulk_communication_campaigns` | Admin-initiated bulk email campaigns with audience filter and message |
| `email_logs` | Every email sent or scheduled; optionally linked to a bulk campaign |
| `audit_logs` | Generic entity-level change log with old/new JSON |
