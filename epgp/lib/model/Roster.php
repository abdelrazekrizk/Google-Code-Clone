<?php

class Roster extends BaseRoster
{
  public function echoLink(){
    echo link_to($this, $this->getRoute());
  }
  
  public function getRoute(){
    return 'character/show?name='.$this->name;
  }
  
  public function getClass(){
    return $this->getClasses()->getName();
  }
  
  public function __toString(){
    return $this->name;
  }
}
