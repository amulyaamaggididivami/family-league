--
-- PostgreSQL database dump
--

\restrict g5UqKv0b5wLOdhkZQVJoHJSbH7nc149cpgSdM7IICTnvjWTeM9afWVTStSKye96

-- Dumped from database version 18.3 (Homebrew)
-- Dumped by pg_dump version 18.3 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: audit_logs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.audit_logs (
    id uuid NOT NULL,
    action character varying(255) NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    entity_id uuid NOT NULL,
    entity_type character varying(255) NOT NULL,
    ip_address character varying(255),
    new_value jsonb,
    old_value jsonb,
    actor_id uuid
);


ALTER TABLE public.audit_logs OWNER TO postgres;

--
-- Name: bulk_communication_campaigns; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bulk_communication_campaigns (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    event_type character varying(255) NOT NULL,
    message_template text,
    recipients_count integer,
    sent_at timestamp(6) with time zone,
    user_filter jsonb,
    admin_id uuid NOT NULL
);


ALTER TABLE public.bulk_communication_campaigns OWNER TO postgres;

--
-- Name: email_logs; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.email_logs (
    id uuid NOT NULL,
    body_ref character varying(255),
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    error_message character varying(255),
    event_type character varying(255) NOT NULL,
    scheduled_at timestamp(6) with time zone,
    sent_at timestamp(6) with time zone,
    status character varying(255) NOT NULL,
    subject character varying(255),
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    campaign_id uuid,
    user_id uuid NOT NULL
);


ALTER TABLE public.email_logs OWNER TO postgres;

--
-- Name: leaderboard_snapshots; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.leaderboard_snapshots (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    rank integer NOT NULL,
    snapshot_at timestamp(6) with time zone NOT NULL,
    total_points integer NOT NULL,
    season_id uuid NOT NULL,
    user_id uuid NOT NULL
);


ALTER TABLE public.leaderboard_snapshots OWNER TO postgres;

--
-- Name: league_predictions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.league_predictions (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    submitted_at timestamp(6) with time zone,
    season_id uuid NOT NULL,
    user_id uuid NOT NULL
);


ALTER TABLE public.league_predictions OWNER TO postgres;

--
-- Name: league_results; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.league_results (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    published_at timestamp(6) with time zone,
    published_by uuid,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    verified_at timestamp(6) with time zone,
    verified_by uuid,
    first_place_team_id uuid,
    season_id uuid NOT NULL,
    second_place_team_id uuid,
    third_place_team_id uuid
);


ALTER TABLE public.league_results OWNER TO postgres;

--
-- Name: league_season_members; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.league_season_members (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    joined_at timestamp(6) with time zone,
    status character varying(255) NOT NULL,
    season_id uuid NOT NULL,
    user_id uuid NOT NULL
);


ALTER TABLE public.league_season_members OWNER TO postgres;

--
-- Name: league_seasons; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.league_seasons (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    end_time timestamp(6) with time zone,
    league_prediction_lock_time timestamp(6) with time zone,
    season_name character varying(255) NOT NULL,
    start_time timestamp(6) with time zone,
    status character varying(255) NOT NULL,
    league_id uuid NOT NULL
);


ALTER TABLE public.league_seasons OWNER TO postgres;

--
-- Name: league_team_predictions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.league_team_predictions (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    predicted_rank integer NOT NULL,
    league_prediction_id uuid NOT NULL,
    team_id uuid NOT NULL
);


ALTER TABLE public.league_team_predictions OWNER TO postgres;

--
-- Name: league_types; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.league_types (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    country character varying(255),
    description character varying(255),
    format character varying(255),
    gender character varying(255),
    governing_body character varying(255),
    is_active boolean NOT NULL,
    name character varying(255) NOT NULL,
    short_code character varying(255) NOT NULL
);


ALTER TABLE public.league_types OWNER TO postgres;

--
-- Name: leagues; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.leagues (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    description character varying(255),
    name character varying(255) NOT NULL,
    season_year integer,
    status character varying(255) NOT NULL,
    league_type_id uuid NOT NULL
);


ALTER TABLE public.leagues OWNER TO postgres;

--
-- Name: match_predictions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.match_predictions (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    submitted_at timestamp(6) with time zone,
    match_id uuid NOT NULL,
    predicted_player_of_match_id uuid,
    predicted_toss_winner_id uuid,
    predicted_winner_id uuid,
    user_id uuid NOT NULL
);


ALTER TABLE public.match_predictions OWNER TO postgres;

--
-- Name: match_results; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.match_results (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    is_tie boolean NOT NULL,
    published_at timestamp(6) with time zone,
    published_by uuid,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    verified_at timestamp(6) with time zone,
    verified_by uuid,
    match_id uuid NOT NULL,
    player_of_match_id uuid,
    toss_winner_team_id uuid,
    winning_team_id uuid
);


ALTER TABLE public.match_results OWNER TO postgres;

--
-- Name: matches; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.matches (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    prediction_lock_time timestamp(6) with time zone NOT NULL,
    scheduled_at timestamp(6) with time zone NOT NULL,
    status character varying(255) NOT NULL,
    venue character varying(255),
    away_team_id uuid NOT NULL,
    home_team_id uuid NOT NULL,
    season_id uuid NOT NULL
);


ALTER TABLE public.matches OWNER TO postgres;

--
-- Name: players; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.players (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    is_active boolean NOT NULL,
    name character varying(255) NOT NULL,
    "position" character varying(255),
    team_id uuid NOT NULL
);


ALTER TABLE public.players OWNER TO postgres;

--
-- Name: prediction_points; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.prediction_points (
    id uuid NOT NULL,
    calculated_at timestamp(6) with time zone NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    point_type character varying(255) NOT NULL,
    points_earned integer NOT NULL,
    source_field character varying(255) NOT NULL,
    league_prediction_id uuid,
    match_id uuid,
    match_prediction_id uuid,
    season_id uuid NOT NULL,
    user_id uuid NOT NULL
);


ALTER TABLE public.prediction_points OWNER TO postgres;

--
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    description character varying(255),
    name character varying(255) NOT NULL,
    updated_at timestamp(6) with time zone,
    updated_by uuid
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- Name: season_teams; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.season_teams (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    season_id uuid NOT NULL,
    team_id uuid NOT NULL
);


ALTER TABLE public.season_teams OWNER TO postgres;

--
-- Name: teams; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.teams (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    logo_url character varying(255),
    name character varying(255) NOT NULL,
    short_code character varying(255) NOT NULL
);


ALTER TABLE public.teams OWNER TO postgres;

--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
    id uuid NOT NULL,
    assigned_at timestamp(6) with time zone NOT NULL,
    assigned_by uuid,
    deleted_at timestamp(6) with time zone,
    is_active boolean NOT NULL,
    role_id uuid NOT NULL,
    user_id uuid NOT NULL
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id uuid NOT NULL,
    created_at timestamp(6) with time zone NOT NULL,
    created_by uuid,
    deleted_at timestamp(6) with time zone,
    updated_at timestamp(6) with time zone,
    updated_by uuid,
    avatar_url character varying(255),
    email character varying(255) NOT NULL,
    full_name character varying(255),
    is_active boolean NOT NULL,
    password_hash character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: audit_logs audit_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit_logs
    ADD CONSTRAINT audit_logs_pkey PRIMARY KEY (id);


--
-- Name: bulk_communication_campaigns bulk_communication_campaigns_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bulk_communication_campaigns
    ADD CONSTRAINT bulk_communication_campaigns_pkey PRIMARY KEY (id);


--
-- Name: email_logs email_logs_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email_logs
    ADD CONSTRAINT email_logs_pkey PRIMARY KEY (id);


--
-- Name: leaderboard_snapshots leaderboard_snapshots_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leaderboard_snapshots
    ADD CONSTRAINT leaderboard_snapshots_pkey PRIMARY KEY (id);


--
-- Name: league_predictions league_predictions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_predictions
    ADD CONSTRAINT league_predictions_pkey PRIMARY KEY (id);


--
-- Name: league_results league_results_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_results
    ADD CONSTRAINT league_results_pkey PRIMARY KEY (id);


--
-- Name: league_season_members league_season_members_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_season_members
    ADD CONSTRAINT league_season_members_pkey PRIMARY KEY (id);


--
-- Name: league_seasons league_seasons_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_seasons
    ADD CONSTRAINT league_seasons_pkey PRIMARY KEY (id);


--
-- Name: league_team_predictions league_team_predictions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_team_predictions
    ADD CONSTRAINT league_team_predictions_pkey PRIMARY KEY (id);


--
-- Name: league_types league_types_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_types
    ADD CONSTRAINT league_types_pkey PRIMARY KEY (id);


--
-- Name: leagues leagues_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leagues
    ADD CONSTRAINT leagues_pkey PRIMARY KEY (id);


--
-- Name: match_predictions match_predictions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_predictions
    ADD CONSTRAINT match_predictions_pkey PRIMARY KEY (id);


--
-- Name: match_results match_results_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_results
    ADD CONSTRAINT match_results_pkey PRIMARY KEY (id);


--
-- Name: matches matches_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT matches_pkey PRIMARY KEY (id);


--
-- Name: players players_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.players
    ADD CONSTRAINT players_pkey PRIMARY KEY (id);


--
-- Name: prediction_points prediction_points_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prediction_points
    ADD CONSTRAINT prediction_points_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: season_teams season_teams_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.season_teams
    ADD CONSTRAINT season_teams_pkey PRIMARY KEY (id);


--
-- Name: teams teams_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teams
    ADD CONSTRAINT teams_pkey PRIMARY KEY (id);


--
-- Name: league_types uk32iplf3n5fng0aatwgjx2y51f; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_types
    ADD CONSTRAINT uk32iplf3n5fng0aatwgjx2y51f UNIQUE (short_code);


--
-- Name: users uk6dotkott2kjsp8vw4d0m25fb7; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT uk6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email);


--
-- Name: league_types uk83oyb9bd52d2sce6wrjrma93p; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_types
    ADD CONSTRAINT uk83oyb9bd52d2sce6wrjrma93p UNIQUE (name);


--
-- Name: match_results ukkntkfia8geb8vqd3vvhtwimqt; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_results
    ADD CONSTRAINT ukkntkfia8geb8vqd3vvhtwimqt UNIQUE (match_id);


--
-- Name: teams ukn79qfeihdfp25t2a2kv9yjg1w; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.teams
    ADD CONSTRAINT ukn79qfeihdfp25t2a2kv9yjg1w UNIQUE (short_code);


--
-- Name: roles ukofx66keruapi6vyqpv6f2or37; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT ukofx66keruapi6vyqpv6f2or37 UNIQUE (name);


--
-- Name: users ukr43af9ap4edm43mmtq01oddj6; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT ukr43af9ap4edm43mmtq01oddj6 UNIQUE (username);


--
-- Name: league_results ukrwfsh7dpb38ypb7klblwa9xn8; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_results
    ADD CONSTRAINT ukrwfsh7dpb38ypb7klblwa9xn8 UNIQUE (season_id);


--
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: season_teams fk1dxfkn0jf196ignroemue1ykw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.season_teams
    ADD CONSTRAINT fk1dxfkn0jf196ignroemue1ykw FOREIGN KEY (season_id) REFERENCES public.league_seasons(id);


--
-- Name: match_results fk1hyyoees13g6eme3pr1t05uic; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_results
    ADD CONSTRAINT fk1hyyoees13g6eme3pr1t05uic FOREIGN KEY (toss_winner_team_id) REFERENCES public.teams(id);


--
-- Name: league_predictions fk27jrey9p5yhd61qq0xpu4wegt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_predictions
    ADD CONSTRAINT fk27jrey9p5yhd61qq0xpu4wegt FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: matches fk2e8erbfecb0tjtq9iudg36bxu; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT fk2e8erbfecb0tjtq9iudg36bxu FOREIGN KEY (away_team_id) REFERENCES public.teams(id);


--
-- Name: prediction_points fk4n99y1hqpyx6bjx668824w1cg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prediction_points
    ADD CONSTRAINT fk4n99y1hqpyx6bjx668824w1cg FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: email_logs fk53riqu0yyo0de9vp07u9n4hbp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email_logs
    ADD CONSTRAINT fk53riqu0yyo0de9vp07u9n4hbp FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: league_results fk5lo6hqocuwi0psqprvplf85qr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_results
    ADD CONSTRAINT fk5lo6hqocuwi0psqprvplf85qr FOREIGN KEY (season_id) REFERENCES public.league_seasons(id);


--
-- Name: players fk5nglidr00c4dyybl171v6kask; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.players
    ADD CONSTRAINT fk5nglidr00c4dyybl171v6kask FOREIGN KEY (team_id) REFERENCES public.teams(id);


--
-- Name: match_predictions fk62gmojk7m18oy8x9p1752ufs6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_predictions
    ADD CONSTRAINT fk62gmojk7m18oy8x9p1752ufs6 FOREIGN KEY (predicted_player_of_match_id) REFERENCES public.players(id);


--
-- Name: matches fk8k68nekawp47js52dq8720voe; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT fk8k68nekawp47js52dq8720voe FOREIGN KEY (home_team_id) REFERENCES public.teams(id);


--
-- Name: prediction_points fk9xskkgla574uqifuyg6jqx932; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prediction_points
    ADD CONSTRAINT fk9xskkgla574uqifuyg6jqx932 FOREIGN KEY (season_id) REFERENCES public.league_seasons(id);


--
-- Name: season_teams fkbvl0b16veuxrwfefe67pmxi6o; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.season_teams
    ADD CONSTRAINT fkbvl0b16veuxrwfefe67pmxi6o FOREIGN KEY (team_id) REFERENCES public.teams(id);


--
-- Name: match_predictions fkc62y2po5o27wecpsktaw4j17w; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_predictions
    ADD CONSTRAINT fkc62y2po5o27wecpsktaw4j17w FOREIGN KEY (predicted_toss_winner_id) REFERENCES public.teams(id);


--
-- Name: league_predictions fkcwq3mlenunlvtfthos6tc31gl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_predictions
    ADD CONSTRAINT fkcwq3mlenunlvtfthos6tc31gl FOREIGN KEY (season_id) REFERENCES public.league_seasons(id);


--
-- Name: match_results fkdba41l0gaxmv8bwhu5o7r7pqt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_results
    ADD CONSTRAINT fkdba41l0gaxmv8bwhu5o7r7pqt FOREIGN KEY (player_of_match_id) REFERENCES public.players(id);


--
-- Name: leaderboard_snapshots fkdch4aeh3nkyi2qgkiwq3pu9sl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leaderboard_snapshots
    ADD CONSTRAINT fkdch4aeh3nkyi2qgkiwq3pu9sl FOREIGN KEY (season_id) REFERENCES public.league_seasons(id);


--
-- Name: match_predictions fkdtw6unubcesogykwhhrcptkvu; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_predictions
    ADD CONSTRAINT fkdtw6unubcesogykwhhrcptkvu FOREIGN KEY (match_id) REFERENCES public.matches(id);


--
-- Name: audit_logs fkf2uqqjmo7gvd8fq7ac81fgc0m; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.audit_logs
    ADD CONSTRAINT fkf2uqqjmo7gvd8fq7ac81fgc0m FOREIGN KEY (actor_id) REFERENCES public.users(id);


--
-- Name: email_logs fkfff9sk4qb3jgy3x0knoa7b3o5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.email_logs
    ADD CONSTRAINT fkfff9sk4qb3jgy3x0knoa7b3o5 FOREIGN KEY (campaign_id) REFERENCES public.bulk_communication_campaigns(id);


--
-- Name: bulk_communication_campaigns fkfj38xqo33ighcyso3b67q9iys; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bulk_communication_campaigns
    ADD CONSTRAINT fkfj38xqo33ighcyso3b67q9iys FOREIGN KEY (admin_id) REFERENCES public.users(id);


--
-- Name: league_season_members fkfu1fg1sc7edndn43w8ptmf6y1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_season_members
    ADD CONSTRAINT fkfu1fg1sc7edndn43w8ptmf6y1 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: match_predictions fkgxdsuuhnhbxu46qkav8wede7i; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_predictions
    ADD CONSTRAINT fkgxdsuuhnhbxu46qkav8wede7i FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: user_roles fkh8ciramu9cc9q3qcqiv4ue8a6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkh8ciramu9cc9q3qcqiv4ue8a6 FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: user_roles fkhfh9dx7w3ubf1co1vdev94g3f; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT fkhfh9dx7w3ubf1co1vdev94g3f FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: league_results fkiidhkawcpj96m5u89c0yb2u8t; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_results
    ADD CONSTRAINT fkiidhkawcpj96m5u89c0yb2u8t FOREIGN KEY (first_place_team_id) REFERENCES public.teams(id);


--
-- Name: matches fkikobaavr29g3beeln59na42rp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matches
    ADD CONSTRAINT fkikobaavr29g3beeln59na42rp FOREIGN KEY (season_id) REFERENCES public.league_seasons(id);


--
-- Name: prediction_points fkiptse7l72fwiycffbxgxouyd5; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prediction_points
    ADD CONSTRAINT fkiptse7l72fwiycffbxgxouyd5 FOREIGN KEY (league_prediction_id) REFERENCES public.league_predictions(id);


--
-- Name: match_results fkj5awqv4fklw0tyjkhrw2fj77v; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_results
    ADD CONSTRAINT fkj5awqv4fklw0tyjkhrw2fj77v FOREIGN KEY (match_id) REFERENCES public.matches(id);


--
-- Name: league_team_predictions fkl2y7uxi628s4csr2g8og1c4qc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_team_predictions
    ADD CONSTRAINT fkl2y7uxi628s4csr2g8og1c4qc FOREIGN KEY (team_id) REFERENCES public.teams(id);


--
-- Name: leagues fklha18ga6bofurdgpo8u8gm6dw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leagues
    ADD CONSTRAINT fklha18ga6bofurdgpo8u8gm6dw FOREIGN KEY (league_type_id) REFERENCES public.league_types(id);


--
-- Name: prediction_points fkmu19u4cl3svavibjd4v15ppib; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prediction_points
    ADD CONSTRAINT fkmu19u4cl3svavibjd4v15ppib FOREIGN KEY (match_prediction_id) REFERENCES public.match_predictions(id);


--
-- Name: league_season_members fkn2qiaiwpbwlgqmk3wot72c9nm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_season_members
    ADD CONSTRAINT fkn2qiaiwpbwlgqmk3wot72c9nm FOREIGN KEY (season_id) REFERENCES public.league_seasons(id);


--
-- Name: prediction_points fknafximwydqg8b6moxpoc081lm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.prediction_points
    ADD CONSTRAINT fknafximwydqg8b6moxpoc081lm FOREIGN KEY (match_id) REFERENCES public.matches(id);


--
-- Name: match_results fkot0owmb8yigjly0118j8610qg; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_results
    ADD CONSTRAINT fkot0owmb8yigjly0118j8610qg FOREIGN KEY (winning_team_id) REFERENCES public.teams(id);


--
-- Name: match_predictions fkov1te1omf3rcqfvupqh400kyw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.match_predictions
    ADD CONSTRAINT fkov1te1omf3rcqfvupqh400kyw FOREIGN KEY (predicted_winner_id) REFERENCES public.teams(id);


--
-- Name: league_seasons fkppkr47vr5perkldk1tcmv4knn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_seasons
    ADD CONSTRAINT fkppkr47vr5perkldk1tcmv4knn FOREIGN KEY (league_id) REFERENCES public.leagues(id);


--
-- Name: leaderboard_snapshots fkqmx98p4ii3kvyp65wad49l8r0; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.leaderboard_snapshots
    ADD CONSTRAINT fkqmx98p4ii3kvyp65wad49l8r0 FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- Name: league_results fkthvbpyyogoul3rafr1jhqkah3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_results
    ADD CONSTRAINT fkthvbpyyogoul3rafr1jhqkah3 FOREIGN KEY (second_place_team_id) REFERENCES public.teams(id);


--
-- Name: league_team_predictions fktmih52lgbl2pgp348oyejcjre; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_team_predictions
    ADD CONSTRAINT fktmih52lgbl2pgp348oyejcjre FOREIGN KEY (league_prediction_id) REFERENCES public.league_predictions(id);


--
-- Name: league_results fktrgphbhmri85kyc1dmbkxi2pl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.league_results
    ADD CONSTRAINT fktrgphbhmri85kyc1dmbkxi2pl FOREIGN KEY (third_place_team_id) REFERENCES public.teams(id);


--
-- PostgreSQL database dump complete
--

\unrestrict g5UqKv0b5wLOdhkZQVJoHJSbH7nc149cpgSdM7IICTnvjWTeM9afWVTStSKye96

