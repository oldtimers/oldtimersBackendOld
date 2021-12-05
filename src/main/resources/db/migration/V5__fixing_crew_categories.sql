create unique index crew_categories_crew_id_category_id_unique_index
    on crew_categories (crew_id, category_id);

drop index crew_categories_ranking_points_index on crew_categories;

create index crew_categories_category_id_ranking_points_index
    on crew_categories (category_id, ranking_points);

