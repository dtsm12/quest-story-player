# quest-story-player
Java library to play an interactive story authored using Quest Markup Language (QuestML)

[![Build Status](https://travis-ci.org/dtsm12/quest-story-player.svg?branch=master)](https://travis-ci.org/dtsm12/quest-story-player)

QuestML Reference: http://questml.com

# QuestML Support

This library supports all QuestML features except those listed below:

* Media elements
* The 'choose' element
* Elements used with the text element
* The following Station elements:
   * embed, table, caption, tr, th, td
* The process attribute of the 'state' element (as per http://questml.com/tutorial/states.htm )

The following behaviour nuances should also be noted:

* When States are evaluated is determined by their position relative to the local 'text' element.
   * e.g. a 'number' element before the 'station', 'if' or 'else' element's text will be evaluated before the text & choices are determined
   * e.g. a 'string' element after the 'station', 'if' or 'else' element's text will be evaluated after the text & choices are determined

# Bespoke enhancements

The following features are planned to enhance how a quest can be played via quest-story-player:

* Inclusion of sound effects within text
* A standard library of sound effects

# Other Players
JavaScript & VisualBasic: http://docs.textadventures.co.uk/quest/
