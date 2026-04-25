-- ============================================================
-- Family League – IPL 2025 Seed Data (idempotent)
-- ============================================================

DO $$
DECLARE
    lt_ipl      UUID;
    lg_ipl25    UUID := gen_random_uuid();
    s_grp       UUID := gen_random_uuid();
    t_mi        UUID := gen_random_uuid();
    t_csk       UUID := gen_random_uuid();
    t_rcb       UUID := gen_random_uuid();
    t_kkr       UUID := gen_random_uuid();
    t_dc        UUID := gen_random_uuid();
    t_srh       UUID := gen_random_uuid();
    t_pbks      UUID := gen_random_uuid();
    t_rr        UUID := gen_random_uuid();

BEGIN

-- ── 1. LEAGUE TYPE (skip if exists, read back the id) ────────
INSERT INTO league_types (id, name, short_code, country, gender, format, governing_body, description, is_active, created_at)
VALUES (gen_random_uuid(), 'Indian Premier League', 'IPL', 'India', 'M', 'T20', 'BCCI', 'Premier T20 franchise league in India', true, now())
ON CONFLICT (name) DO NOTHING;

SELECT id INTO lt_ipl FROM league_types WHERE name = 'Indian Premier League';

-- ── 2. TEAMS ─────────────────────────────────────────────────
INSERT INTO teams (id, name, short_code, logo_url, created_at) VALUES
    (t_mi,   'Mumbai Indians',               'MI',   '', now()),
    (t_csk,  'Chennai Super Kings',          'CSK',  '', now()),
    (t_rcb,  'Royal Challengers Bengaluru',  'RCB',  '', now()),
    (t_kkr,  'Kolkata Knight Riders',        'KKR',  '', now()),
    (t_dc,   'Delhi Capitals',               'DC',   '', now()),
    (t_srh,  'Sunrisers Hyderabad',          'SRH',  '', now()),
    (t_pbks, 'Punjab Kings',                 'PBKS', '', now()),
    (t_rr,   'Rajasthan Royals',             'RR',   '', now())
ON CONFLICT (short_code) DO NOTHING;

-- Re-read team ids in case some already existed
SELECT id INTO t_mi   FROM teams WHERE short_code = 'MI';
SELECT id INTO t_csk  FROM teams WHERE short_code = 'CSK';
SELECT id INTO t_rcb  FROM teams WHERE short_code = 'RCB';
SELECT id INTO t_kkr  FROM teams WHERE short_code = 'KKR';
SELECT id INTO t_dc   FROM teams WHERE short_code = 'DC';
SELECT id INTO t_srh  FROM teams WHERE short_code = 'SRH';
SELECT id INTO t_pbks FROM teams WHERE short_code = 'PBKS';
SELECT id INTO t_rr   FROM teams WHERE short_code = 'RR';

-- ── 3. LEAGUE ─────────────────────────────────────────────────
INSERT INTO leagues (id, league_type_id, name, season_year, status, description, created_at)
VALUES (lg_ipl25, lt_ipl, 'IPL 2025', 2025, 'UPCOMING', 'Indian Premier League 2025 edition', now())
ON CONFLICT DO NOTHING;

-- ── 4. LEAGUE SEASON ──────────────────────────────────────────
INSERT INTO league_seasons (id, league_id, season_name, status, league_prediction_lock_time, start_time, end_time, created_at)
VALUES (
    s_grp, lg_ipl25,
    'IPL 2025 – Group Stage',
    'UPCOMING',
    '2025-03-22 04:30:00+00',
    '2025-03-22 08:30:00+00',
    '2025-05-25 14:00:00+00',
    now()
)
ON CONFLICT DO NOTHING;

-- ── 5. ADD TEAMS TO SEASON ────────────────────────────────────
INSERT INTO season_teams (id, season_id, team_id, created_at)
SELECT gen_random_uuid(), s_grp, t, now()
FROM (VALUES (t_mi),(t_csk),(t_rcb),(t_kkr),(t_dc),(t_srh),(t_pbks),(t_rr)) AS v(t)
WHERE NOT EXISTS (
    SELECT 1 FROM season_teams WHERE season_id = s_grp AND team_id = v.t
);

-- ── 6. PLAYERS ────────────────────────────────────────────────
INSERT INTO players (id, team_id, name, position, is_active, created_at)
SELECT gen_random_uuid(), team_id, name, position, true, now()
FROM (VALUES
    (t_mi,  'Rohit Sharma',         'BATSMAN'),
    (t_mi,  'Jasprit Bumrah',       'BOWLER'),
    (t_mi,  'Suryakumar Yadav',     'BATSMAN'),
    (t_mi,  'Hardik Pandya',        'ALL_ROUNDER'),
    (t_mi,  'Ishan Kishan',         'WICKETKEEPER'),
    (t_csk, 'MS Dhoni',             'WICKETKEEPER'),
    (t_csk, 'Ruturaj Gaikwad',      'BATSMAN'),
    (t_csk, 'Ravindra Jadeja',      'ALL_ROUNDER'),
    (t_csk, 'Deepak Chahar',        'BOWLER'),
    (t_csk, 'Devon Conway',         'BATSMAN'),
    (t_rcb, 'Virat Kohli',          'BATSMAN'),
    (t_rcb, 'Glenn Maxwell',        'ALL_ROUNDER'),
    (t_rcb, 'Mohammed Siraj',       'BOWLER'),
    (t_rcb, 'Faf du Plessis',       'BATSMAN'),
    (t_rcb, 'Dinesh Karthik',       'WICKETKEEPER'),
    (t_kkr, 'Shreyas Iyer',         'BATSMAN'),
    (t_kkr, 'Andre Russell',        'ALL_ROUNDER'),
    (t_kkr, 'Sunil Narine',         'ALL_ROUNDER'),
    (t_kkr, 'Varun Chakravarthy',   'BOWLER'),
    (t_kkr, 'Phil Salt',            'WICKETKEEPER'),
    (t_dc,  'David Warner',         'BATSMAN'),
    (t_dc,  'Rishabh Pant',         'WICKETKEEPER'),
    (t_dc,  'Axar Patel',           'ALL_ROUNDER'),
    (t_dc,  'Anrich Nortje',        'BOWLER'),
    (t_dc,  'Mitchell Marsh',       'ALL_ROUNDER'),
    (t_srh, 'Pat Cummins',          'ALL_ROUNDER'),
    (t_srh, 'Heinrich Klaasen',     'WICKETKEEPER'),
    (t_srh, 'Travis Head',          'BATSMAN'),
    (t_srh, 'Bhuvneshwar Kumar',    'BOWLER'),
    (t_srh, 'Abdul Samad',          'ALL_ROUNDER'),
    (t_pbks,'Shikhar Dhawan',       'BATSMAN'),
    (t_pbks,'Sam Curran',           'ALL_ROUNDER'),
    (t_pbks,'Arshdeep Singh',       'BOWLER'),
    (t_pbks,'Jonny Bairstow',       'WICKETKEEPER'),
    (t_pbks,'Kagiso Rabada',        'BOWLER'),
    (t_rr,  'Sanju Samson',         'WICKETKEEPER'),
    (t_rr,  'Jos Buttler',          'BATSMAN'),
    (t_rr,  'Yuzvendra Chahal',     'BOWLER'),
    (t_rr,  'Ravichandran Ashwin',  'ALL_ROUNDER'),
    (t_rr,  'Trent Boult',          'BOWLER')
) AS v(team_id, name, position)
WHERE NOT EXISTS (
    SELECT 1 FROM players WHERE team_id = v.team_id AND name = v.name
);

RAISE NOTICE 'Seed complete.';
END $$;
