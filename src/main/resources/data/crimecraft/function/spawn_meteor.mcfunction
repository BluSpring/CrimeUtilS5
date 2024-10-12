summon minecraft:block_display ~15 ~50 ~ {Tags:["meteor"],block_state:{Name:"minecraft:obsidian"},transformation:{translation:[0,0,0],left_rotation:[0,0,0,1],right_rotation:[0,0,0,1],scale:[5,5,5]},teleport_duration:59}
tp @e[tag=meteor] ~ ~ ~

stopsound @a master
stopsound @a ambient
stopsound @a music
stopsound @a block
stopsound @a hostile
stopsound @a neutral
stopsound @a player
stopsound @a record
stopsound @a voice
stopsound @a weather

summon marker ~ ~ ~ {Tags:["meteor_pos"]}
playsound crimecraft:meteor_strike master @a ~ ~ ~ 15 1 1

schedule function crimecraft:place_meteor 50