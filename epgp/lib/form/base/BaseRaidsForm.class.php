<?php

/**
 * Raids form base class.
 *
 * @package    epgp
 * @subpackage form
 * @author     Your name here
 * @version    SVN: $Id: sfPropelFormGeneratedTemplate.php 15484 2009-02-13 13:13:51Z fabien $
 */
class BaseRaidsForm extends BaseFormPropel
{
  public function setup()
  {
    $this->setWidgets(array(
      'id'             => new sfWidgetFormInputHidden(),
      'note'           => new sfWidgetFormInput(),
      'boss'           => new sfWidgetFormPropelChoice(array('model' => 'Bosses', 'add_empty' => false)),
      'baseval'        => new sfWidgetFormInput(),
      'inflatedval'    => new sfWidgetFormInput(),
      'date'           => new sfWidgetFormDateTime(),
      'attendees_list' => new sfWidgetFormPropelChoiceMany(array('model' => 'Roster')),
    ));

    $this->setValidators(array(
      'id'             => new sfValidatorPropelChoice(array('model' => 'Raids', 'column' => 'id', 'required' => false)),
      'note'           => new sfValidatorString(array('max_length' => 512)),
      'boss'           => new sfValidatorPropelChoice(array('model' => 'Bosses', 'column' => 'id')),
      'baseval'        => new sfValidatorNumber(),
      'inflatedval'    => new sfValidatorNumber(),
      'date'           => new sfValidatorDateTime(),
      'attendees_list' => new sfValidatorPropelChoiceMany(array('model' => 'Roster', 'required' => false)),
    ));

    $this->widgetSchema->setNameFormat('raids[%s]');

    $this->errorSchema = new sfValidatorErrorSchema($this->validatorSchema);

    parent::setup();
  }

  public function getModelName()
  {
    return 'Raids';
  }


  public function updateDefaultsFromObject()
  {
    parent::updateDefaultsFromObject();

    if (isset($this->widgetSchema['attendees_list']))
    {
      $values = array();
      foreach ($this->object->getAttendeess() as $obj)
      {
        $values[] = $obj->getRosterId();
      }

      $this->setDefault('attendees_list', $values);
    }

  }

  protected function doSave($con = null)
  {
    parent::doSave($con);

    $this->saveAttendeesList($con);
  }

  public function saveAttendeesList($con = null)
  {
    if (!$this->isValid())
    {
      throw $this->getErrorSchema();
    }

    if (!isset($this->widgetSchema['attendees_list']))
    {
      // somebody has unset this widget
      return;
    }

    if (is_null($con))
    {
      $con = $this->getConnection();
    }

    $c = new Criteria();
    $c->add(AttendeesPeer::RAIDS_ID, $this->object->getPrimaryKey());
    AttendeesPeer::doDelete($c, $con);

    $values = $this->getValue('attendees_list');
    if (is_array($values))
    {
      foreach ($values as $value)
      {
        $obj = new Attendees();
        $obj->setRaidsId($this->object->getPrimaryKey());
        $obj->setRosterId($value);
        $obj->save();
      }
    }
  }

}
