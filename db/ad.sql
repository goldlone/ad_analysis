-- 用户广告点击量统计
CREATE TABLE `ad_user_click_count` (
  `date` varchar(30) NOT NULL,
  `user_id` int(11) NOT NULL,
  `ad_id` int(11) NOT NULL,
  `click_count` int(11) DEFAULT 0,
  primary key (date, user_id, ad_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 广告用户黑名单
CREATE TABLE `ad_blacklist` (
  `user_id` int(11) NOT NULL,
  primary key (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 每天各省各城市各广告的点击量
CREATE TABLE `ad_stat` (
  `date` varchar(30) NOT NULL,
  `province` varchar(100) NOT NULL,
  `city` varchar(100) NOT NULL,
  `ad_id` int(11) NOT NULL,
  `click_count` int(11) DEFAULT 0,
  primary key (date, province, city, ad_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 热门广告Top3
CREATE TABLE `ad_province_top3` (
  `date` varchar(30) NOT NULL,
  `province` varchar(100) NOT NULL,
  `ad_id` int(11) NOT NULL,
  `click_count` int(11) DEFAULT 0,
  primary key (date, province, ad_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 广告趋势
CREATE TABLE `ad_click_trend` (
  `date` varchar(30) NOT NULL,
  `ad_id` int(11) NOT NULL,
  `minute` varchar(30) NOT NULL,
  `click_count` int(11) DEFAULT 0,
  primary key (date, ad_id, minute)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 广告推送量
CREATE TABLE `ad_put_count` (
  `date` VARCHAR(30) NOT NULL,
  `ad_id` INT(11) NOT NULL,
  `count` INT(11) DEFAULT 0,
  PRIMARY KEY (date, ad_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 广告点击量
CREATE TABLE `ad_click_count` (
  `date` VARCHAR(30) NOT NULL,
  `ad_id` INT(11) NOT NULL,
  `count` INT(11) DEFAULT 0,
  PRIMARY KEY (date, ad_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 广告交易量
CREATE TABLE `ad_deal_count` (
  `date` VARCHAR(30) NOT NULL,
  `ad_id` INT(11) NOT NULL,
  `count` INT(11) DEFAULT 0,
  PRIMARY KEY (date, ad_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- CTV
CREATE TABLE `ad_ctv` (
  `date` VARCHAR(30) NOT NULL,
  `ad_id` INT(11) NOT NULL,
  `ctv` DOUBLE DEFAULT 0.00,
  PRIMARY KEY (date, ad_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- CRV
CREATE TABLE `ad_crv` (
  `date` VARCHAR(30) NOT NULL,
  `ad_id` INT(11) NOT NULL,
  `crv` DOUBLE DEFAULT 0.00,
  PRIMARY KEY (date, ad_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------
-- 使用触发器算
-- ----------

create trigger trigger_ad_ctv_put_insert
  after insert on ad_put_count
  for each row
  begin
    call calc_ad_ctv(new.date, new.ad_id);
  end;
create trigger trigger_ad_ctv_put_update
  after update on ad_put_count
  for each row
  begin
    call calc_ad_ctv(new.date, new.ad_id);
  end;
create trigger trigger_ad_ctv_click_insert
  after insert on ad_click_count
  for each row
  begin
    call calc_ad_ctv(new.date, new.ad_id);
    call calc_ad_crv(new.date, new.ad_id);
  end;
create trigger trigger_ad_ctv_click_update
  after update on ad_click_count
  for each row
  begin
    call calc_ad_ctv(new.date, new.ad_id);
    call calc_ad_crv(new.date, new.ad_id);
  end;
create trigger trigger_ad_ctv_deal_insert
  after insert on ad_deal_count
  for each row
  begin
    call calc_ad_crv(new.date, new.ad_id);
  end;
create trigger trigger_ad_ctv_deal_update
  after update on ad_deal_count
  for each row
  begin
    call calc_ad_crv(new.date, new.ad_id);
  end;


-- 计算 广告点击率
drop procedure if exists calc_ad_ctv;
create procedure calc_ad_ctv(dt varchar(255), id int)
begin
  select count
  into @click_count
  from ad_click_count
  where date=dt and
        ad_id=id;

  select count
  into @put_count
  from ad_put_count
  where date=dt and
        ad_id=id;

  if @click_count is not null then
    set @ctv = @click_count / @put_count;

    insert
    into ad_ctv(date, ad_id, ctv)
    values(dt, id, @ctv)
    on duplicate key update ctv = @ctv;
  end if;
end;


-- 计算广告转化率
drop procedure if exists calc_ad_crv;
create procedure calc_ad_crv(dt varchar(30), id int)
  begin
    select count
    into @click_count
    from ad_click_count
    where date=dt and
          ad_id=id;

    select count
    into @deal_count
    from ad_deal_count
    where date=dt and
          ad_id=id;

    if @deal_count is not null then
      set @crv = @deal_count / @click_count;

      insert
      into ad_crv(date, ad_id, crv)
      values(dt, id, @crv)
      on duplicate key update crv = @crv;
    end if;
  end;



