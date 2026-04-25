-- ============================================================
-- IPL 2025 – Match Schedule Seed (idempotent)
-- prediction_lock_time = scheduled_at - 1 hour
-- All times in UTC (14:00 IST = 08:30 UTC, 19:30 IST = 14:00 UTC)
-- ============================================================

DO $$
DECLARE
    s_grp   UUID;
    t_mi    UUID;
    t_csk   UUID;
    t_rcb   UUID;
    t_kkr   UUID;
    t_dc    UUID;
    t_srh   UUID;
    t_pbks  UUID;
    t_rr    UUID;
BEGIN

-- Read existing IDs
SELECT id INTO s_grp  FROM league_seasons WHERE season_name = 'IPL 2025 – Group Stage';
SELECT id INTO t_mi   FROM teams WHERE short_code = 'MI';
SELECT id INTO t_csk  FROM teams WHERE short_code = 'CSK';
SELECT id INTO t_rcb  FROM teams WHERE short_code = 'RCB';
SELECT id INTO t_kkr  FROM teams WHERE short_code = 'KKR';
SELECT id INTO t_dc   FROM teams WHERE short_code = 'DC';
SELECT id INTO t_srh  FROM teams WHERE short_code = 'SRH';
SELECT id INTO t_pbks FROM teams WHERE short_code = 'PBKS';
SELECT id INTO t_rr   FROM teams WHERE short_code = 'RR';

-- Insert matches, skip if same home+away+season+scheduled_at already exists
INSERT INTO matches (id, season_id, home_team_id, away_team_id, venue, scheduled_at, prediction_lock_time, status, created_at)
SELECT gen_random_uuid(), s_grp, home, away, venue,
       scheduled_at::timestamptz,
       (scheduled_at::timestamptz - interval '1 hour'),
       'SCHEDULED',
       now()
FROM (VALUES
    -- Day 1  (22 Mar) – evening match 19:30 IST = 14:00 UTC
    (t_mi,   t_csk,  'Wankhede Stadium, Mumbai',        '2025-03-22 14:00:00+00'),
    -- Day 2  (23 Mar)
    (t_rcb,  t_kkr,  'M Chinnaswamy Stadium, Bengaluru','2025-03-23 14:00:00+00'),
    -- Day 3  (24 Mar)
    (t_dc,   t_srh,  'Arun Jaitley Stadium, Delhi',     '2025-03-24 14:00:00+00'),
    -- Day 4  (25 Mar)
    (t_pbks, t_rr,   'IS Bindra Stadium, Mohali',       '2025-03-25 14:00:00+00'),
    -- Day 5  (26 Mar) – double header
    (t_csk,  t_rcb,  'MA Chidambaram Stadium, Chennai', '2025-03-26 08:30:00+00'),
    (t_mi,   t_kkr,  'Wankhede Stadium, Mumbai',        '2025-03-26 14:00:00+00'),
    -- Day 6  (27 Mar)
    (t_srh,  t_rr,   'Rajiv Gandhi Intl Stadium, Hyd',  '2025-03-27 14:00:00+00'),
    -- Day 7  (28 Mar)
    (t_dc,   t_pbks, 'Arun Jaitley Stadium, Delhi',     '2025-03-28 14:00:00+00'),
    -- Day 8  (29 Mar)
    (t_kkr,  t_csk,  'Eden Gardens, Kolkata',           '2025-03-29 14:00:00+00'),
    -- Day 9  (30 Mar)
    (t_rr,   t_mi,   'Sawai Mansingh Stadium, Jaipur',  '2025-03-30 14:00:00+00'),
    -- Day 10 (31 Mar)
    (t_rcb,  t_srh,  'M Chinnaswamy Stadium, Bengaluru','2025-03-31 14:00:00+00'),
    -- Day 11 (01 Apr) – double header
    (t_pbks, t_kkr,  'IS Bindra Stadium, Mohali',       '2025-04-01 08:30:00+00'),
    (t_csk,  t_dc,   'MA Chidambaram Stadium, Chennai', '2025-04-01 14:00:00+00'),
    -- Day 12 (02 Apr)
    (t_mi,   t_srh,  'Wankhede Stadium, Mumbai',        '2025-04-02 14:00:00+00')
) AS v(home, away, venue, scheduled_at)
WHERE NOT EXISTS (
    SELECT 1 FROM matches
    WHERE season_id    = s_grp
      AND home_team_id = v.home
      AND away_team_id = v.away
      AND scheduled_at = v.scheduled_at::timestamptz
);

RAISE NOTICE '% matches seeded.', (SELECT count(*) FROM matches WHERE season_id = s_grp);
END $$;
