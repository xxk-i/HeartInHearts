# @a(ll)
# quick fadein, stay for 5 seconds, single second fade out
title @a times 1 100 20

# clear subtitle text
title @a title {"text":""}

# actual title is set by our DeathEvent.java so we can easily include death name
# so we just schedule the rest
schedule function hih:death/dethklok3 1s
schedule function hih:death/dethklok2 2s
schedule function hih:death/dethklok1 3s
schedule function hih:death/dethklokbye 4s
schedule function hih:death/killall 5s