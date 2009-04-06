<?php

/**
 * Roster form.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormTemplate.php 10377 2008-07-21 07:10:32Z dwhittle $
 */
class RosterForm extends BaseRosterForm
{
  public function configure()
  {
    unset(
      $this['ep'], $this['gp'], $this['priority']
    );
  }
}
