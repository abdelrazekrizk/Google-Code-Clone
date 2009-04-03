<?php

class Attendees extends BaseAttendees
{
  public function echoLink(){
    $this->getRoster()->echoLink();
  }
  
  public function getClass(){
    return $this->getRoster()->getClass();
  }
}
