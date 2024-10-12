execute at @e[tag=meteor_pos] run place structure stellaris:meteor
kill @e[tag=meteor]
execute at @e[tag=meteor_pos] run title @a[distance=..64] times 0 150 35
execute at @e[tag=meteor_pos] run title @a[distance=..64] title {"text":"\ue001","font":"crimecraft:screen"}

schedule function crimecraft:welp 15